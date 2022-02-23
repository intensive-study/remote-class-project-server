package org.server.remoteclass.service.student;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.server.remoteclass.dto.lecture.ResponseLectureFromStudentDto;
import org.server.remoteclass.dto.student.RequestStudentDto;
import org.server.remoteclass.dto.student.ResponseStudentByLecturerDto;
import org.server.remoteclass.entity.Lecture;
import org.server.remoteclass.entity.Student;
import org.server.remoteclass.entity.User;
import org.server.remoteclass.exception.*;
import org.server.remoteclass.jpa.LectureRepository;
import org.server.remoteclass.jpa.StudentRepository;
import org.server.remoteclass.jpa.UserRepository;
import org.server.remoteclass.util.AccessVerification;
import org.server.remoteclass.util.BeanConfiguration;
import org.server.remoteclass.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
public class StudentServiceImpl implements StudentService{

    private final UserRepository userRepository;
    private final LectureRepository lectureRepository;
    private final StudentRepository studentRepository;
    private final ModelMapper modelMapper;
    private final AccessVerification accessVerification;

    @Autowired
    public StudentServiceImpl(UserRepository userRepository, LectureRepository lectureRepository,
                              StudentRepository studentRepository, BeanConfiguration beanConfiguration,
                              AccessVerification accessVerification){
        this.userRepository = userRepository;
        this.lectureRepository = lectureRepository;
        this.studentRepository = studentRepository;
        this.modelMapper = beanConfiguration.modelMapper();
        this.accessVerification = accessVerification;
    }

    @Override
    @Transactional
    public void createStudent(RequestStudentDto requestStudentDto) {
        User user = SecurityUtil.getCurrentUserEmail()
                .flatMap(userRepository::findByEmail)
                .orElseThrow(() -> new IdNotExistException("현재 로그인 상태가 아닙니다.", ErrorCode.ID_NOT_EXIST));

        if(!studentRepository.existsByLecture_LectureIdAndUser_UserId(requestStudentDto.getLectureId(), user.getUserId())) {
            Student student = new Student();
            student.setUser(user);
            Lecture lecture = lectureRepository.findById(requestStudentDto.getLectureId())
                    .orElseThrow(() -> new IdNotExistException("존재하지 않는 강의입니다.", ErrorCode.ID_NOT_EXIST));
            student.setLecture(lecture);
            studentRepository.save(student);
        }
        else{
            throw new BadRequestArgumentException("이미 신청한 강의입니다.", ErrorCode.BAD_REQUEST_ARGUMENT);
        }
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
        else{
            throw new IdNotExistException("취소할 강의가 없습니다.", ErrorCode.ID_NOT_EXIST);
        }
    }

    //현재 수강생의 수강 강좌 리스트 조회
    @Override
    public List<ResponseLectureFromStudentDto> getLecturesByUserId() {

        User user = SecurityUtil.getCurrentUserEmail()
                .flatMap(userRepository::findByEmail)
                .orElseThrow(() -> new IdNotExistException("현재 로그인 상태가 아닙니다.", ErrorCode.ID_NOT_EXIST));

        List<Student> students = studentRepository.findByUser_UserIdOrderByLecture_StartDateDesc(user.getUserId());

        return students.stream().map(ResponseLectureFromStudentDto::new).collect(Collectors.toList());
    }

    //강좌별 전체 수강생 목록
    @Override
    public List<ResponseStudentByLecturerDto> getStudentsByLectureId(Long lectureId) {

        lectureRepository.findById(lectureId).orElseThrow(() -> new IdNotExistException("존재하지 않는 강의입니다.", ErrorCode.ID_NOT_EXIST));

        //강의자 본인 권한이거나 관리자 권한일때
//        if(user.getUserId() == lecture.getUser().getUserId()){
        List<Student> students = studentRepository.findByLecture_LectureId(lectureId);

        return students.stream().map(student-> ResponseStudentByLecturerDto.from(student)).collect(Collectors.toList());
    }

    //특정 수강생의 수강 강좌 리스트 조회
    @Override
    public List<ResponseLectureFromStudentDto> getLecturesByUserIdByAdmin(Long userId)  {

        List<Student> students = studentRepository.findByUser_UserIdOrderByLecture_StartDateDesc(userId);

        return students.stream().map(ResponseLectureFromStudentDto::new).collect(Collectors.toList());
    }

}