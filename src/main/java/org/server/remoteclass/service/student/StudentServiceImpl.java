package org.server.remoteclass.service.student;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.server.remoteclass.constant.Authority;
import org.server.remoteclass.constant.UserRole;
import org.server.remoteclass.dto.lecture.ResponseLectureDto;
import org.server.remoteclass.dto.student.RequestStudentDto;
import org.server.remoteclass.dto.student.ResponseStudentByLecturerDto;
import org.server.remoteclass.dto.student.StudentDto;
import org.server.remoteclass.entity.Lecture;
import org.server.remoteclass.entity.Student;
import org.server.remoteclass.entity.User;
import org.server.remoteclass.exception.ForbiddenException;
import org.server.remoteclass.exception.IdNotExistException;
import org.server.remoteclass.exception.EmailDuplicateException;
import org.server.remoteclass.exception.ErrorCode;
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

@Slf4j
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
    public StudentDto createStudent(RequestStudentDto requestStudentDto) {
        User user = SecurityUtil.getCurrentUserEmail()
                .flatMap(userRepository::findByEmail)
                .orElseThrow(() -> new IdNotExistException("현재 로그인 상태가 아닙니다.", ErrorCode.ID_NOT_EXIST));
        Student student = new Student();
        if(user.getUserRole() == UserRole.ROLE_STUDENT){
            if(!studentRepository.existsByLecture_LectureIdAndUser_UserId(requestStudentDto.getLectureId(), user.getUserId())) {
                student.setUser(user);
                student.setLecture(lectureRepository.findById(requestStudentDto.getLectureId()).orElse(null));
            }
            else{
                throw new EmailDuplicateException("이미 가입된 이메일 주소입니다.", ErrorCode.EMAIL_DUPLICATION);
            }
        }
        return StudentDto.from(studentRepository.save(student));
    }

    @Override
    @Transactional
    public void cancel(Long lectureId) {
        User user = SecurityUtil.getCurrentUserEmail()
                .flatMap(userRepository::findByEmail)
                .orElseThrow(() -> new IdNotExistException("현재 로그인 상태가 아닙니다.", ErrorCode.ID_NOT_EXIST));
        if(studentRepository.existsByLecture_LectureIdAndUser_UserId(lectureId, user.getUserId())){
            studentRepository.deleteByLecture_LectureIdAndUser_UserId(lectureId, user.getUserId());
        }
    }

    //현재 수강생의 수강 강좌 리스트 조회
    @Override
    public List<ResponseLectureDto> getLecturesByUserId() {

        User user = SecurityUtil.getCurrentUserEmail()
                .flatMap(userRepository::findByEmail)
                .orElseThrow(() -> new IdNotExistException("현재 로그인 상태가 아닙니다.", ErrorCode.ID_NOT_EXIST));

        List<Student> students = null;
        if(user.getUserRole() == UserRole.ROLE_STUDENT){
            students = studentRepository.findByUser_UserIdOrderByLecture_StartDateDesc(user.getUserId());
        }
        else{
            throw new ForbiddenException("접근 권한이 없습니다.", ErrorCode.FORBIDDEN);
        }
        return students.stream().map(lecture->ResponseLectureDto.from(lecture)).collect(Collectors.toList());
    }

    //강좌별 전체 수강생 목록
    @Override
    public List<ResponseStudentByLecturerDto> getStudentsByLectureId(Long lectureId) {

        User user = SecurityUtil.getCurrentUserEmail()
                .flatMap(userRepository::findByEmail)
                .orElseThrow(() -> new IdNotExistException("현재 로그인 상태가 아닙니다.", ErrorCode.ID_NOT_EXIST));

        Lecture lecture = lectureRepository.findById(lectureId).orElse(null);
        List<Student> students = null;
        //강의자 본인 권한이거나 관리자 권한일때
        if((user.getUserRole() == UserRole.ROLE_STUDENT && Objects.equals(user.getUserId(), lecture.getUser().getUserId()))
                || (user.getAuthority() == Authority.ROLE_ADMIN)){
            students = studentRepository.findByLecture_LectureId(lectureId);
        }
        else{
            throw new ForbiddenException("접근 권한이 없습니다", ErrorCode.FORBIDDEN);
        }
        return students.stream().map(student-> ResponseStudentByLecturerDto.from(student)).collect(Collectors.toList());
    }

    //특정 수강생의 수강 강좌 리스트 조회
    @Override
    public List<ResponseLectureDto> getLecturesByUserIdByAdmin(Long userId)  {

        User user = SecurityUtil.getCurrentUserEmail()
                .flatMap(userRepository::findByEmail)
                .orElseThrow(() -> new IdNotExistException("현재 로그인 상태가 아닙니다.", ErrorCode.ID_NOT_EXIST));

        List<Student> students = null;
        if(user.getAuthority() == Authority.ROLE_ADMIN){
            students = studentRepository.findByUser_UserIdOrderByLecture_StartDateDesc(userId);
        }
        else{
            throw new ForbiddenException("접근 권한이 없습니다.", ErrorCode.FORBIDDEN);
        }
        return students.stream().map(ResponseLectureDto::new).collect(Collectors.toList());
    }

}