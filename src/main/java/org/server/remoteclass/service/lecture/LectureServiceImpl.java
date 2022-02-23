package org.server.remoteclass.service.lecture;

import org.modelmapper.ModelMapper;

import org.server.remoteclass.constant.UserRole;
import org.server.remoteclass.dto.lecture.RequestModifyLectureDto;
import org.server.remoteclass.dto.lecture.RequestLectureDto;
import org.server.remoteclass.dto.lecture.ResponseLectureDto;
import org.server.remoteclass.entity.Category;
import org.server.remoteclass.entity.Lecture;
import org.server.remoteclass.entity.User;
import org.server.remoteclass.exception.ForbiddenException;
import org.server.remoteclass.exception.IdNotExistException;
import org.server.remoteclass.exception.ErrorCode;
import org.server.remoteclass.jpa.CategoryRepository;
import org.server.remoteclass.jpa.LectureRepository;
import org.server.remoteclass.jpa.UserRepository;
import org.server.remoteclass.util.AccessVerification;
import org.server.remoteclass.util.BeanConfiguration;
import org.server.remoteclass.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class LectureServiceImpl implements LectureService{

    private final UserRepository userRepository;
    private final LectureRepository lectureRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final AccessVerification accessVerification;

    @Autowired
    public LectureServiceImpl(UserRepository userRepository,
                              LectureRepository lectureRepository,
                              CategoryRepository categoryRepository,
                              BeanConfiguration beanConfiguration,
                              AccessVerification accessVerification
                              ){
        this.userRepository = userRepository;
        this.lectureRepository = lectureRepository;
        this.categoryRepository = categoryRepository;
        this.modelMapper = beanConfiguration.modelMapper();
        this.accessVerification = accessVerification;
    }

    //강의 생성
    @Override
    @Transactional
    public void createLecture(RequestLectureDto requestLectureDto) {
        User user = SecurityUtil.getCurrentUserEmail()
                .flatMap(userRepository::findByEmail)
                .orElseThrow(() -> new IdNotExistException("현재 로그인 상태가 아닙니다.", ErrorCode.ID_NOT_EXIST));

        Lecture lecture = modelMapper.map(requestLectureDto, Lecture.class);
        Category category = categoryRepository.findById(requestLectureDto.getCategoryId())
                .orElseThrow(() -> new IdNotExistException("카테고리 존재하지 않음", ErrorCode.ID_NOT_EXIST));

        lecture.setCategory(category);
        lecture.setUser(user);
        lectureRepository.save(lecture);
    }

    //특정 강의 조회
    @Override
    public ResponseLectureDto getLectureByLectureId(Long lectureId) {
        return ResponseLectureDto.from(lectureRepository.findById(lectureId).orElse(null));
    }

    //강의 수정
    @Override
    @Transactional
    public void updateLecture(RequestModifyLectureDto requestModifyLectureDto) {
        User user = SecurityUtil.getCurrentUserEmail()
                .flatMap(userRepository::findByEmail)
                .orElseThrow(() -> new IdNotExistException("현재 로그인 상태가 아닙니다.", ErrorCode.ID_NOT_EXIST));

        Lecture lecture = lectureRepository.findById(requestModifyLectureDto.getLectureId())
                .orElseThrow(() -> new IdNotExistException("해당 강의가 존재하지 않습니다.", ErrorCode.ID_NOT_EXIST));

        //로그인한 userId와 수정할 강좌의 userId가 같지않으면 수정권한 없음.
        if(user.getUserId() != lecture.getUser().getUserId()){
            throw new ForbiddenException("강의 수정 권한이 없습니다.", ErrorCode.FORBIDDEN);
        }

        Category category = categoryRepository.findById(requestModifyLectureDto.getCategoryId())
                .orElseThrow(() -> new IdNotExistException("해당 카테고리가 존재하지 않습니다.", ErrorCode.ID_NOT_EXIST));

        Lecture modifiedLecture = Lecture.builder()
                        .lectureId(requestModifyLectureDto.getLectureId())
                        .title(requestModifyLectureDto.getTitle())
                        .description(requestModifyLectureDto.getDescription())
                        .price(requestModifyLectureDto.getPrice())
                        .startDate(requestModifyLectureDto.getStartDate())
                        .endDate(requestModifyLectureDto.getEndDate())
                        .category(category).user(user).build();
        lectureRepository.save(modifiedLecture);
    }

    //강의 삭제
    @Override
    @Transactional
    public void deleteLecture(Long lectureId){
        User user = SecurityUtil.getCurrentUserEmail()
                .flatMap(userRepository::findByEmail)
                .orElseThrow(() -> new IdNotExistException("현재 로그인 상태가 아닙니다.", ErrorCode.ID_NOT_EXIST));

        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new IdNotExistException("해당 강의가 존재하지 않습니다.", ErrorCode.ID_NOT_EXIST));

        if(user.getUserId() != lecture.getUser().getUserId()){
            throw new ForbiddenException("강의 삭제 권한이 없습니다.", ErrorCode.FORBIDDEN);
        }
        lectureRepository.deleteById(lectureId);
    }

    //강의 전체 조회
    @Override
    public List<ResponseLectureDto> getLectureByAll() {
        List<Lecture> lectures = lectureRepository.findAll();
        return lectures.stream().map(lecture->ResponseLectureDto.from(lecture)).collect(Collectors.toList());
    }

    //카테고리별 강의 조회
    @Override
    public List<ResponseLectureDto> getLectureByCategoryId(Long categoryId){
        categoryRepository.findById(categoryId).orElseThrow(()->new IdNotExistException("해당 카테고리가 존재하지 않습니다.", ErrorCode.ID_NOT_EXIST));
        List<Lecture> lectures = lectureRepository.findByCategory_CategoryId(categoryId);
        return lectures.stream().map(lecture->ResponseLectureDto.from(lecture)).collect(Collectors.toList());
    }
}