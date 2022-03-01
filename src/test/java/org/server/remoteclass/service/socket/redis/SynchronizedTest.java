package org.server.remoteclass.service.socket.redis;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.server.remoteclass.constant.Authority;
import org.server.remoteclass.constant.UserRole;
import org.server.remoteclass.entity.Category;
import org.server.remoteclass.entity.Lecture;
import org.server.remoteclass.entity.Student;
import org.server.remoteclass.entity.User;
import org.server.remoteclass.jpa.CategoryRepository;
import org.server.remoteclass.jpa.LectureRepository;
import org.server.remoteclass.jpa.StudentRepository;
import org.server.remoteclass.jpa.UserRepository;
import org.server.remoteclass.jpa.socket.SessionRepository;
import org.server.remoteclass.service.socket.CommonRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.server.remoteclass.util.EncryptString.changeLongToString;

@SpringBootTest
public class SynchronizedTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    LectureRepository lectureRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    SessionRepository sessionRepository;

    @Autowired
    CommonRequest commonRequest;

    @Autowired
    PlatformTransactionManager platformTransactionManager;

    TransactionTemplate transactionTemplate;

    User teacher;
    Lecture lecture;
    Category category;
    User firstStudent;
    User secondStudent;

    Student student1;
    Student student2;

    @BeforeEach
    public void setUp() {
        /**
         * 스레드를 생성해서 돌리면 다른 컨넥션을 공유하기때문에 실제로 테스트 데이터 값을 디비에 저장하지 않는 이상,
         * 권한 에러 발생(디비엔 실제로 테스트를 위해 생성한 엔티티의 값이 저장되지 않는다.)
         * 아니면 엔티티 생성 후, 디비 반영을 위해 flush를 직접 해줘야 한다.
         *
         * 고로 실제에 디비에 아래 아이디를 갖는 데이터를 저장하고, 테스트를 진행한다.
         * */

        teacher = new User();
        teacher.setEmail("teacher@gmail.com");
        teacher.setName("teacher");
        teacher.setPassword("teacher1234");
        teacher.setUserRole(UserRole.ROLE_LECTURER);
        teacher.setAuthority(Authority.ROLE_USER);

        firstStudent = new User();
        firstStudent.setEmail("firstStudent@gmail.com");
        firstStudent.setName("firstStudent");
        firstStudent.setPassword("firstStudent1234");
        firstStudent.setUserRole(UserRole.ROLE_STUDENT);
        firstStudent.setAuthority(Authority.ROLE_USER);

        secondStudent = new User();
        secondStudent.setEmail("secondStudent@gmail.com");
        secondStudent.setName("secondStudent");
        secondStudent.setPassword("secondStudent1234");
        secondStudent.setUserRole(UserRole.ROLE_STUDENT);
        secondStudent.setAuthority(Authority.ROLE_USER);

        lecture = new Lecture();
        lecture.setTitle("lecture");
        lecture.setUser(teacher);
        lecture.setPrice(10000);
        lecture.setStartDate(LocalDateTime.now());
        lecture.setEndDate(LocalDateTime.now().plusDays(10));

        category = new Category();
        category.setCategoryName("category");

        lecture.setCategory(category);

        student1 = new Student();
        student1.setUser(firstStudent);
        student1.setLecture(lecture);

        student2 = new Student();
        student2.setUser(secondStudent);
        student2.setLecture(lecture);

    }

    @AfterEach
    public void setDown() {
        sessionRepository.clearAll();
    }


    @Test
    public void enterLiveSynchronizedTest() throws InterruptedException {

        int numberOfThreads = 2;
        transactionTemplate = new TransactionTemplate(platformTransactionManager);

        transactionTemplate.execute(status -> {
            saveEntity();
            commonRequest.startLive(teacher.getUserId(), lecture.getLectureId());
            return null;
        });

        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        executorService.execute(() -> transactionTemplate.execute(status -> {
            commonRequest.enterLive(firstStudent.getUserId(), lecture.getLectureId());
            return null;
        }));
        executorService.execute(() -> transactionTemplate.execute(status -> {
            commonRequest.enterLive(secondStudent.getUserId(), lecture.getLectureId());
            return null;
        }));

        executorService.shutdown();
        executorService.awaitTermination(10, TimeUnit.SECONDS);

        transactionTemplate.execute(status -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e){
                System.out.println("e = " + e);
            }
            // 추가된 학생 + 강사
            Assertions.assertThat(sessionRepository.getConnectionsByLectureId(changeLongToString(lecture.getLectureId())).size())
                    .isEqualTo(numberOfThreads + 1);

            commonRequest.exitLive(teacher.getUserId(), lecture.getLectureId());

            // 모두 제거된 상태
            Assertions.assertThat(sessionRepository.containsLectureSessionOnSessionManager(changeLongToString(lecture.getLectureId())))
                    .isFalse();
            return null;
        });

    }


    @Test //note that there is no @Transactional configured for the method
    public void enterWaitingRoomSynchronizedTest() throws InterruptedException {
        int numberOfThreads = 2;
        transactionTemplate = new TransactionTemplate(platformTransactionManager);

        transactionTemplate.execute(status -> {
            saveEntity();
            return null;
        });

        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        executorService.execute(() -> transactionTemplate.execute(status -> {
            commonRequest.enterWaitingRoom(firstStudent.getUserId(), lecture.getLectureId());
            return null;
        }));
        executorService.execute(() -> transactionTemplate.execute(status -> {
            commonRequest.enterWaitingRoom(secondStudent.getUserId(), lecture.getLectureId());
            return null;
        }));

        executorService.shutdown();
        executorService.awaitTermination(10, TimeUnit.SECONDS);

        transactionTemplate.execute(status -> {
            // validate test results in transaction
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e){
                System.out.println("e = " + e);
            }

            Assertions.assertThat(sessionRepository.getConnectionsOnWaitingRoom(changeLongToString(lecture.getLectureId())).size())
                    .isEqualTo(2);

            commonRequest.startLive(teacher.getUserId(), lecture.getLectureId());
            commonRequest.exitLive(teacher.getUserId(), lecture.getLectureId());
            return null;
        });

    }

    public void saveEntity() {
        userRepository.save(teacher);
        userRepository.save(firstStudent);
        userRepository.save(secondStudent);

        lectureRepository.save(lecture);
        categoryRepository.save(category);

        studentRepository.save(student1);
        studentRepository.save(student2);

    }
}
