package org.server.remoteclass.service;

import org.modelmapper.ModelMapper;

import org.server.remoteclass.dto.LectureDto;
import org.server.remoteclass.entity.Category;
import org.server.remoteclass.entity.Lecture;
import org.server.remoteclass.entity.User;
import org.server.remoteclass.exception.ForbiddenException;
import org.server.remoteclass.exception.IdNotExistException;
import org.server.remoteclass.exception.ResultCode;
import org.server.remoteclass.jpa.CategoryRepository;
import org.server.remoteclass.jpa.LectureRepository;
import org.server.remoteclass.jpa.UserRepository;
import org.server.remoteclass.util.BeanConfiguration;
import org.server.remoteclass.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessApiUsageException;
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

    @Autowired
    public LectureServiceImpl(UserRepository userRepository,
                              LectureRepository lectureRepository,
                              CategoryRepository categoryRepository,
                              BeanConfiguration beanConfiguration){
        this.userRepository = userRepository;
        this.lectureRepository = lectureRepository;
        this.categoryRepository = categoryRepository;
        this.modelMapper = beanConfiguration.modelMapper();
    }

    //강의 생성
    @Override
    @Transactional
    public LectureDto createLecture(LectureDto lectureDto) throws IdNotExistException, ForbiddenException {
        User user = SecurityUtil.getCurrentUserEmail()
                .flatMap(userRepository::findByEmail)
                .orElseThrow(() -> new IdNotExistException("존재하지 않는 사용자", ResultCode.ID_NOT_EXIST));

        // User의 Role이 Lecturer인 경우 강의 생성, 아니면 403 코드 반환
//        if(user.getUserRole() != UserRole.ROLE_LECTURER){
//            throw new ForbiddenException("접근 권한이 없습니다.", ResultCode.FORBIDDEN);
//        }
        Lecture lecture = modelMapper.map(lectureDto, Lecture.class);
        Category category = categoryRepository.findById(lectureDto.getCategoryId())
                .orElseThrow(() -> new IdNotExistException("카테고리 존재하지 않음", ResultCode.ID_NOT_EXIST));
        lecture.setCategory(category);
        lecture.setUser(user);

        return LectureDto.from(lectureRepository.save(lecture));
    }

    //특정 강의 조회
    @Override
    public LectureDto getLectureByLectureId(Long lectureId) {
        return LectureDto.from(lectureRepository.findById(lectureId).orElse(null));
    }

    //강의 수정
    @Override
    @Transactional
    public LectureDto updateLecture(LectureDto lectureDto) throws IdNotExistException{
        User user = SecurityUtil.getCurrentUserEmail()
                .flatMap(userRepository::findByEmail)
                .orElseThrow(() -> new IdNotExistException("존재하지 않는 사용자", ResultCode.ID_NOT_EXIST));

        Lecture lecture = lectureRepository.findById(lectureDto.getLectureId())
                .orElseThrow(() -> new IdNotExistException("존재하지 않는 강의", ResultCode.ID_NOT_EXIST));

        //로그인한 userId와 수정할 강좌의 userId가 같지않으면 수정권한 없음.
        if(user.getUserId() != lecture.getUser().getUserId()){
            throw new InvalidDataAccessApiUsageException("수정 권한이 없습니다.");
        }
        Category category = categoryRepository.findById(lectureDto.getCategoryId())
                .orElseThrow(() -> new IdNotExistException("존재하지 않는 카테고리", ResultCode.ID_NOT_EXIST));

        Lecture modifiedLecture = Lecture.builder()
                        .lectureId(lectureDto.getLectureId()).title(lectureDto.getTitle())
                        .description(lectureDto.getDescription()).price(lectureDto.getPrice())
                        .startDate(lectureDto.getStartDate()).endDate(lectureDto.getEndDate())
                        .category(category).user(user).build();

        return LectureDto.from(lectureRepository.save(modifiedLecture));
    }

    //강의 삭제
    @Override
    @Transactional
    public void deleteLecture(Long lectureId) throws IdNotExistException{
        User user = SecurityUtil.getCurrentUserEmail()
                .flatMap(userRepository::findByEmail)
                .orElseThrow(() -> new IdNotExistException("존재하지 않는 사용자", ResultCode.ID_NOT_EXIST));

        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new IdNotExistException("존재하지 않는 강의", ResultCode.ID_NOT_EXIST));

        if(user.getUserId() != lecture.getUser().getUserId()){
            throw new InvalidDataAccessApiUsageException("삭제 권한이 없습니다.");
        }
        lectureRepository.deleteById(lectureId);
    }

    //강의 전체 조회
    @Override
    public List<LectureDto> getLectureByAll() {
        List<Lecture> lectures = lectureRepository.findAll();
        return lectures.stream().map(lecture->LectureDto.from(lecture)).collect(Collectors.toList());
    }

    //카테고리별 강의 조회
    @Override
    public List<LectureDto> getLectureByCategoryId(Long categoryId) throws IdNotExistException{
        categoryRepository.findById(categoryId).orElseThrow(()->new IdNotExistException("존재하지 않는 카테고리", ResultCode.ID_NOT_EXIST));
        List<Lecture> lectures = lectureRepository.findByCategory_CategoryId(categoryId);
        return lectures.stream().map(lecture->LectureDto.from(lecture)).collect(Collectors.toList());
    }
}