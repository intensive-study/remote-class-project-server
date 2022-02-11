package org.server.remoteclass.service;

import org.modelmapper.ModelMapper;
import org.server.remoteclass.constant.UserRole;
import org.server.remoteclass.dto.LectureDto;
import org.server.remoteclass.dto.StudentDto;
import org.server.remoteclass.dto.UserDto;
import org.server.remoteclass.entity.Lecture;
import org.server.remoteclass.entity.Student;
import org.server.remoteclass.entity.User;
import org.server.remoteclass.exception.ForbiddenException;
import org.server.remoteclass.exception.IdNotExistException;
import org.server.remoteclass.exception.NameDuplicateException;
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
    public StudentDto applyLecture(StudentDto studentDto) throws IdNotExistException, NameDuplicateException {
        User user = SecurityUtil.getCurrentUserEmail()
                .flatMap(userRepository::findByEmail)
                .orElseThrow(() -> new IdNotExistException("존재하지 않는 사용자", ResultCode.ID_NOT_EXIST));
        Student student = modelMapper.map(studentDto, Student.class);
        if(user.getUserRole() == UserRole.ROLE_STUDENT){
            if(!studentRepository.existsByLecture_LectureIdAndUser_UserId(studentDto.getLectureId(), studentDto.getUserId())) {
                student.setUser(user);
                student.setLecture(lectureRepository.findById(studentDto.getLectureId()).orElse(null));
            }
            else{
                throw new NameDuplicateException("이미 존재하는 데이터입니다.", ResultCode.NAME_DUPLICATION);
            }
        }
        return StudentDto.from(studentRepository.save(student));
    }

    @Override
    @Transactional
    public void cancel(Long lectureId) throws IdNotExistException {
        User user = SecurityUtil.getCurrentUserEmail()
                .flatMap(userRepository::findByEmail)
                .orElseThrow(() -> new IdNotExistException("존재하지 않는 사용자", ResultCode.ID_NOT_EXIST));
        if(studentRepository.existsByLecture_LectureIdAndUser_UserId(lectureId, user.getUserId())){
            studentRepository.deleteByLecture_LectureIdAndUser_UserId(lectureId, user.getUserId());
        }
    }

    //강좌별 전체 수강생 목록
    @Override
    public List<UserDto> getStudentsByLectureId(Long lectureId) throws IdNotExistException, ForbiddenException {

        User user = SecurityUtil.getCurrentUserEmail()
                .flatMap(userRepository::findByEmail)
                .orElseThrow(() -> new IdNotExistException("존재하지 않는 사용자", ResultCode.ID_NOT_EXIST));

        Lecture lecture = lectureRepository.findById(lectureId).orElse(null);
        List<Student> students = null;
        if(user.getUserRole() == UserRole.ROLE_STUDENT && Objects.equals(user.getUserId(), lecture.getUser().getUserId())){
            students = studentRepository.findByLecture_LectureId(lectureId);
        }
        else{
            throw new ForbiddenException("접근 권한이 없습니다", ResultCode.FORBIDDEN);
        }
        return students.stream().map(student-> UserDto.from(student)).collect(Collectors.toList());
    }


    //현재 수강생의 수강 강좌 리스트 조회
    @Override
    public List<LectureDto> getLecturesByUserId() throws IdNotExistException, ForbiddenException {

        User user = SecurityUtil.getCurrentUserEmail()
                .flatMap(userRepository::findByEmail)
                .orElseThrow(() -> new IdNotExistException("존재하지 않는 사용자", ResultCode.ID_NOT_EXIST));

        List<Student> students = null;
        if(user.getUserRole() == UserRole.ROLE_STUDENT){
            students = studentRepository.findByUser_UserIdOrderByLecture_StartDateDesc(user.getUserId());
        }
        else{
            throw new ForbiddenException("접근 권한이 없습니다.", ResultCode.FORBIDDEN);
        }
        return students.stream().map(lecture->LectureDto.from(lecture)).collect(Collectors.toList());
    }

}