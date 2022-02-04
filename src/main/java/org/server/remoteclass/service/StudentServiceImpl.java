package org.server.remoteclass.service;

import org.modelmapper.ModelMapper;
import org.server.remoteclass.dto.LectureDto;
import org.server.remoteclass.dto.StudentDto;
import org.server.remoteclass.dto.StudentFormDto;
import org.server.remoteclass.dto.UserDto;
import org.server.remoteclass.entity.Lecture;
import org.server.remoteclass.entity.Student;
import org.server.remoteclass.entity.User;
import org.server.remoteclass.exception.IdNotExistException;
import org.server.remoteclass.exception.ResultCode;
import org.server.remoteclass.jpa.LectureRepository;
import org.server.remoteclass.jpa.StudentRepository;
import org.server.remoteclass.jpa.UserRepository;
import org.server.remoteclass.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class StudentServiceImpl implements StudentService{

    private final UserRepository userRepository;
    private final LectureRepository lectureRepository;
    private final StudentRepository studentRepository;

    @Autowired
    public StudentServiceImpl(UserRepository userRepository, LectureRepository lectureRepository,StudentRepository studentRepository){
        this.userRepository = userRepository;
        this.lectureRepository = lectureRepository;
        this.studentRepository = studentRepository;
    }

    @Override
    @Transactional
    public StudentDto applyLecture(StudentFormDto studentFormDto) throws IdNotExistException{

        // 이 부분이 3번 반복이라, 공통적으로 뺄 수 있으면 좋을 것 같습니다. 생성자에서 초기화 해도 되는지 모르겠네요.
        User user = SecurityUtil.getCurrentUserEmail()
                .flatMap(userRepository::findByEmail)
                .orElseThrow(() -> new IdNotExistException("존재하지 않는 사용자", ResultCode.ID_NOT_EXIST));
//        //학생 권한인지 확인하고
//        if(user.getUserRole() == USER_STUDENT){
//            //학생 권한이면 강의번호 있는지 확인한다.
            ////강의도 존재하면
            Student student = new ModelMapper().map(studentFormDto, Student.class);
            student.setUser(user);
            student.setLecture(lectureRepository.findById(studentFormDto.getLectureId()).orElse(null));
            return StudentDto.from(studentRepository.save(student));
        }

    //강좌별 전체 수강생 목록
    @Override
    public List<StudentDto> getStudentsByLectureId(Long lectureId) throws IdNotExistException{

        User user = SecurityUtil.getCurrentUserEmail()
                .flatMap(userRepository::findByEmail)
                .orElseThrow(() -> new IdNotExistException("존재하지 않는 사용자", ResultCode.ID_NOT_EXIST));

        //현재 사용자 권한이 강의자이고, 해당 강의의 author와 같다면 조회 가능
//        if(user.getRole()==USER_LECTURER && user.getUserId() == lecture.getUser().getUserId()){
        ModelMapper mapper = new ModelMapper();
        List<Student> students = studentRepository.findAll();
        //수강생이 없는 강의도 있다고 생각해서 예외처리 부분 지웠어요. 새로 만든 강의의 경우 수강생이 0명일 수 밖에 없다고 생각했어요.
        return students.stream().map(student -> mapper.map(student, StudentDto.class)).collect(Collectors.toList());
    }


    //현재 수강생의 수강 강좌 리스트 조회
    @Override
    public List<LectureDto> getLecturesByUserId() throws IdNotExistException{
        //현재 사용자 확인
        User user = SecurityUtil.getCurrentUserEmail()
                .flatMap(userRepository::findByEmail)
                .orElseThrow(() -> new IdNotExistException("존재하지 않는 사용자", ResultCode.ID_NOT_EXIST));
        // 현재 사용자의 userId를 가진 student들을 조회
        ModelMapper mapper = new ModelMapper();
        Collection<Lecture> lectures = studentRepository.findByStudentId(user.getUserId());
        return lectures.stream().map(lecture -> mapper.map(lecture, LectureDto.class)).collect(Collectors.toList());
    }

}