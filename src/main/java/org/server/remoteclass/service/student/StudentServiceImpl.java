package org.server.remoteclass.service.student;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.server.remoteclass.dto.lecture.ResponseLectureFromStudentDto;
import org.server.remoteclass.dto.student.ResponseStudentByLecturerDto;
import org.server.remoteclass.entity.*;
import org.server.remoteclass.exception.*;
import org.server.remoteclass.jpa.*;
import org.server.remoteclass.util.AccessVerification;
import org.server.remoteclass.util.BeanConfiguration;
import org.server.remoteclass.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
public class StudentServiceImpl implements StudentService{

    private final UserRepository userRepository;
    private final LectureRepository lectureRepository;
    private final StudentRepository studentRepository;
    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;
    private final AccessVerification accessVerification;
    private PurchaseRepository purchaseRepository;

    @Autowired
    public StudentServiceImpl(UserRepository userRepository,
                              LectureRepository lectureRepository,
                              StudentRepository studentRepository,
                              OrderRepository orderRepository,
                              PurchaseRepository purchaseRepository,
                              BeanConfiguration beanConfiguration,
                              AccessVerification accessVerification){
        this.userRepository = userRepository;
        this.lectureRepository = lectureRepository;
        this.studentRepository = studentRepository;
        this.orderRepository = orderRepository;
        this.purchaseRepository = purchaseRepository;
        this.modelMapper = beanConfiguration.modelMapper();
        this.accessVerification = accessVerification;
    }


    //?????? ???????????? ?????? ?????? ????????? ??????
    @Override
    public List<ResponseLectureFromStudentDto> getLecturesByUserId() {

        User user = SecurityUtil.getCurrentUserEmail()
                .flatMap(userRepository::findByEmail)
                .orElseThrow(() -> new IdNotExistException("?????? ????????? ????????? ????????????.", ErrorCode.ID_NOT_EXIST));

        List<Student> students = studentRepository.findByUser_UserIdOrderByLecture_StartDateDesc(user.getUserId());

        return students.stream().map(ResponseLectureFromStudentDto::new).collect(Collectors.toList());
    }

    //????????? ?????? ????????? ??????
    @Override
    public List<ResponseStudentByLecturerDto> getStudentsByLectureId(Long lectureId) {
        User user = SecurityUtil.getCurrentUserEmail()
                .flatMap(userRepository::findByEmail)
                .orElseThrow(() -> new IdNotExistException("?????? ????????? ????????? ????????????.", ErrorCode.ID_NOT_EXIST));

        Lecture lecture = lectureRepository.findById(lectureId).orElseThrow(() -> new IdNotExistException("???????????? ?????? ???????????????.", ErrorCode.ID_NOT_EXIST));

        //????????? ?????? ????????????
        List<Student> students = new ArrayList<>();
        if(user.getUserId() == lecture.getUser().getUserId()) {
            students = studentRepository.findByLecture_LectureId(lectureId);
        }
        else{
            throw new ForbiddenException("?????? ????????? ????????????.", ErrorCode.FORBIDDEN);
        }
        return students.stream().map(student-> ResponseStudentByLecturerDto.from(student)).collect(Collectors.toList());
    }

    //?????? ???????????? ?????? ?????? ????????? ??????
    @Override
    public List<ResponseLectureFromStudentDto> getLecturesByUserIdByAdmin(Long userId)  {

        List<Student> students = studentRepository.findByUser_UserIdOrderByLecture_StartDateDesc(userId);

        return students.stream().map(ResponseLectureFromStudentDto::new).collect(Collectors.toList());
    }

    //????????? ?????? ????????? ?????? (?????????)
    @Override
    public List<ResponseStudentByLecturerDto> getStudentsByLectureIdByAdmin(Long lectureId) {
        lectureRepository.findById(lectureId).orElseThrow(() -> new IdNotExistException("???????????? ?????? ???????????????.", ErrorCode.ID_NOT_EXIST));

        //????????? ?????? ????????????
        List<Student> students = studentRepository.findByLecture_LectureId(lectureId);
        return students.stream().map(student-> ResponseStudentByLecturerDto.from(student)).collect(Collectors.toList());
    }
  
    @Override
    public Boolean checkIfUserIsStudentInLecture(Long lectureId, Long userId) {
        return studentRepository.existsByLecture_LectureIdAndUser_UserId(lectureId, userId);
    }
}