package org.server.remoteclass.service;

import org.modelmapper.ModelMapper;
import org.server.remoteclass.dto.StudentFormDto;
import org.server.remoteclass.entity.Lecture;
import org.server.remoteclass.entity.Student;
import org.server.remoteclass.entity.UserEntity;
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
public class StudentServiceImpl implements StudentService{

    UserRepository userRepository;
    LectureRepository lectureRepository;
    StudentRepository studentRepository;

    @Autowired
    public StudentServiceImpl(UserRepository userRepository, LectureRepository lectureRepository,StudentRepository studentRepository){
        this.userRepository = userRepository;
        this.lectureRepository = lectureRepository;
        this.studentRepository = studentRepository;
    }


    @Override
    public Student applyLecture(StudentFormDto studentFormDto) throws IdNotExistException{
        UserEntity user = SecurityUtil.getCurrentUserEmail()
                .flatMap(userRepository::findOneWithAuthoritiesByEmail)
                .orElseThrow(() -> new IdNotExistException("존재하지 않는 사용자", ResultCode.ID_NOT_EXIST));

//        //학생 권한인지 확인하고
//        if(user.getUserRole() == USER_STUDENT){
//            //학생 권한이면 강의번호 있는지 확인한다.
            Lecture lecture = lectureRepository.findById(studentFormDto.getLectureId())
                    .orElseThrow(()->new IdNotExistException("존재하지 않는 강의", ResultCode.ID_NOT_EXIST));
            ////강의도 존재하면
            Student student = new ModelMapper().map(studentFormDto, Student.class);
            student.setUser(user);
            student.setLecture(lecture);
            Student result = studentRepository.save(student);
            return result;
        }
//        else{
//            throw new InvalidDataAccessApiUsageException("수강 신청 권한 없음");
//        }
//    }

    //강좌별 전체 수강생 목록
    @Override
    @Transactional(readOnly = true)
    public List<Student> getStudentsByLectureId(Long lectureId) throws IdNotExistException{
        Collection<Student> students;

        UserEntity user = SecurityUtil.getCurrentUserEmail()
                .flatMap(userRepository::findOneWithAuthoritiesByEmail)
                .orElseThrow(() -> new IdNotExistException("존재하지 않는 사용자", ResultCode.ID_NOT_EXIST));
        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new IdNotExistException("존재하지 않는 강의", ResultCode.ID_NOT_EXIST));

        //현재 사용자 권한이 강의자이고, 해당 강의의 author와 같다면 조회 가능
//        if(user.getRole()==USER_LECTURER && user.getUserId() == lecture.getUser().getUserId()){
            students = studentRepository.findByLectureId(lectureId); // 수강생이 존재하지 않는 예외 필요
            if(students.isEmpty()){
                throw new IllegalStateException("수강생이 존재하지 않는 강의");
            }
//        }
//        else{
//            throw new InvalidDataAccessApiUsageException("조회 권한이 없습니다.");
//        }
        return students.stream().collect(Collectors.toList());
    }


    //현재 수강생의 수강 강좌 리스트 조회
    @Override
    @Transactional(readOnly = true)
    public List<Student> getLecturesByUserId() throws IdNotExistException{
        Collection<Student> students;
        //현재 사용자 확인
        UserEntity user = SecurityUtil.getCurrentUserEmail()
                .flatMap(userRepository::findOneWithAuthoritiesByEmail)
                .orElseThrow(() -> new IdNotExistException("존재하지 않는 사용자", ResultCode.ID_NOT_EXIST));
        // 현재 사용자의 userId를 가진 student들을 조회
        students = studentRepository.findByUserId(user.getUserId());
        if(students.isEmpty()){
            throw new IllegalStateException("신청한 강의가 없습니다.");
        }
        return students.stream().collect(Collectors.toList());
    }

}