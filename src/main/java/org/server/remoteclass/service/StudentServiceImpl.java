package org.server.remoteclass.service;

import org.modelmapper.ModelMapper;
import org.server.remoteclass.dto.LectureDto;
import org.server.remoteclass.dto.StudentDto;
import org.server.remoteclass.dto.StudentFormDto;
import org.server.remoteclass.entity.Lecture;
import org.server.remoteclass.entity.Student;
import org.server.remoteclass.entity.User;
import org.server.remoteclass.entity.UserRole;
import org.server.remoteclass.exception.IdNotExistException;
import org.server.remoteclass.exception.ResultCode;
import org.server.remoteclass.jpa.LectureRepository;
import org.server.remoteclass.jpa.StudentRepository;
import org.server.remoteclass.jpa.UserRepository;
import org.server.remoteclass.util.BeanConfiguration;
import org.server.remoteclass.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class StudentServiceImpl implements StudentService{

    private final UserRepository userRepository;
    private final LectureRepository lectureRepository;
    private final StudentRepository studentRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public StudentServiceImpl(UserRepository userRepository, LectureRepository lectureRepository,
                              StudentRepository studentRepository, BeanConfiguration beanConfiguration){
        this.userRepository = userRepository;
        this.lectureRepository = lectureRepository;
        this.studentRepository = studentRepository;
        this.modelMapper = beanConfiguration.modelMapper();
    }

    @Override
    @Transactional
    public StudentDto applyLecture(StudentFormDto studentFormDto) throws IdNotExistException{

        // 이 부분이 3번 반복이라, 공통적으로 뺄 수 있으면 좋을 것 같습니다. 생성자에서 초기화 해도 되는지 모르겠네요.
        User user = SecurityUtil.getCurrentUserEmail()
                .flatMap(userRepository::findByEmail)
                .orElseThrow(() -> new IdNotExistException("존재하지 않는 사용자", ResultCode.ID_NOT_EXIST));
        Student student = modelMapper.map(studentFormDto, Student.class);
        if(user.getUserRole() == UserRole.ROLE_STUDENT){
            student.setUser(user);
            student.setLecture(lectureRepository.findById(studentFormDto.getLectureId()).orElse(null));
        }
        return StudentDto.from(studentRepository.save(student));
    }

    //강좌별 전체 수강생 목록
    @Override
    public List<StudentDto> getStudentsByLectureId(Long lectureId) throws IdNotExistException{

        User user = SecurityUtil.getCurrentUserEmail()
                .flatMap(userRepository::findByEmail)
                .orElseThrow(() -> new IdNotExistException("존재하지 않는 사용자", ResultCode.ID_NOT_EXIST));

        Lecture lecture = lectureRepository.findById(lectureId).orElse(null);
        List<Student> students = null;
        if(user.getUserRole() == UserRole.ROLE_LECTURER && Objects.equals(user.getUserId(), lecture.getUser().getUserId())){
            students = studentRepository.findByLectureId(lectureId);
        }
        //수강생이 없는 강의도 있다고 생각해서 예외처리 부분 지웠어요. 새로 만든 강의의 경우 수강생이 0명일 수 밖에 없다고 생각했어요.
        return students.stream().map(student -> modelMapper.map(student, StudentDto.class))
                .collect(Collectors.toList());
    }


    //현재 수강생의 수강 강좌 리스트 조회
    @Override
    public List<LectureDto> getLecturesByUserId() throws IdNotExistException{
        //현재 사용자 확인
        User user = SecurityUtil.getCurrentUserEmail()
                .flatMap(userRepository::findByEmail)
                .orElseThrow(() -> new IdNotExistException("존재하지 않는 사용자", ResultCode.ID_NOT_EXIST));
        // 현재 사용자의 userId를 가진 student들을 조회
        List<Student> students = null;
        if(user.getUserRole() == UserRole.ROLE_STUDENT){
            students = studentRepository.findByUser_UserIdOrderByLecture_StartDateDesc(user.getUserId());
        }
//        List<Lecture> lectures = studentRepository.findByStudentId(user.getUserId());
        return students.stream().map(lecture -> modelMapper.map(lecture, LectureDto.class)).collect(Collectors.toList());
    }

}