package org.server.remoteclass.service;

import org.modelmapper.ModelMapper;

import org.server.remoteclass.dto.LectureDto;
import org.server.remoteclass.dto.LectureFormDto;
import org.server.remoteclass.entity.Category;
import org.server.remoteclass.entity.Lecture;
import org.server.remoteclass.entity.User;
import org.server.remoteclass.exception.IdNotExistException;
import org.server.remoteclass.exception.ResultCode;
import org.server.remoteclass.jpa.CategoryRepository;
import org.server.remoteclass.jpa.LectureRepository;
import org.server.remoteclass.jpa.UserRepository;
import org.server.remoteclass.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class LectureServiceImpl implements LectureService{

    UserRepository userRepository;
    LectureRepository lectureRepository;
    CategoryRepository categoryRepository;

    @Autowired
    public LectureServiceImpl(UserRepository userRepository, LectureRepository lectureRepository,CategoryRepository categoryRepository){
        this.userRepository = userRepository;
        this.lectureRepository = lectureRepository;
        this.categoryRepository = categoryRepository;
    }

    /**
     * 강의 생성
     * USER_LECTURER만 가능
     */
    @Override
    @Transactional
    public LectureDto createLecture(LectureFormDto lectureFormDto) throws IdNotExistException {
        User user = SecurityUtil.getCurrentUserEmail()
                .flatMap(userRepository::findByEmail)
                .orElseThrow(() -> new IdNotExistException("존재하지 않는 사용자", ResultCode.ID_NOT_EXIST));

        // 스프링 시큐리티 컨텍스트에 저장된 User의 Role이 Lecturer인 경우 강의 생성, 아니면 403 반환
//        if(user.getUserRole() != USER_LECTURER){
//            throw new InvalidDataAccessApiUsageException("권한 없음");
//        }
        Lecture lecture = new ModelMapper().map(lectureFormDto, Lecture.class);
        Category category = categoryRepository.findById(lectureFormDto.getCategoryId())
                .orElseThrow(() -> new IdNotExistException("카테고리 존재하지 않음", ResultCode.ID_NOT_EXIST));
        lecture.setCategory(category);
        lecture.setUser(user);

        return LectureDto.from(lectureRepository.save(lecture));
    }
    /**
     * 특정 강의 조회
     * @param : lectureId
     */
    @Override
    public LectureDto getLectureByLectureId(Long lectureId) throws IdNotExistException {
        return lectureRepository.findById(lectureId)
                .orElseThrow(() -> new IdNotExistException("존재하지 않는 강의", ResultCode.ID_NOT_EXIST));
    }

    /**
     * 강의 수정
     * 생성자만 수정 가능
     */
    @Override
    @Transactional
    public LectureDto updateLecture(LectureFormDto lectureFormDto) throws IdNotExistException{
        //check user and authorities
        User user = SecurityUtil.getCurrentUserEmail()
                .flatMap(userRepository::findByEmail)
                .orElseThrow(() -> new IdNotExistException("존재하지 않는 사용자", ResultCode.ID_NOT_EXIST));

        Lecture lecture = lectureRepository.findById(lectureFormDto.getLectureId())
                .orElseThrow(() -> new IdNotExistException("존재하지 않는 강의", ResultCode.ID_NOT_EXIST));

        //로그인한 userId와 수정할 강좌의 userId가 같지않으면 수정권한 없음.
        if(user.getUserId() != lecture.getUser().getUserId()){
            throw new InvalidDataAccessApiUsageException("수정 권한이 없습니다.");
        }
        Category category = categoryRepository.findById(lectureFormDto.getCategoryId())
                .orElseThrow(() -> new IdNotExistException("존재하지 않는 카테고리", ResultCode.ID_NOT_EXIST));;

        lecture.setTitle(lectureFormDto.getTitle());
        lecture.setDescription(lectureFormDto.getDescription());
        lecture.setPrice(lectureFormDto.getPrice());
        lecture.setStartDate(lectureFormDto.getStartDate());
        lecture.setEndDate(lectureFormDto.getEndDate());
        lecture.setCategory(category);
        lecture.setUser(user);

        return LectureDto.from(lecture);
    }

    /**
     * 강의 삭제
     * @param lectureId
     * 강의 저자만 삭제 가능
     */

    @Override
    @Transactional
    public void deleteLecture(Long lectureId) throws IdNotExistException{
        //check user and check if user has delete authority
        User user = SecurityUtil.getCurrentUserEmail()
                .flatMap(userRepository::findByEmail)
                .orElseThrow(() -> new IdNotExistException("존재하지 않는 사용자", ResultCode.ID_NOT_EXIST));

        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new IdNotExistException("존재하지 않는 강의", ResultCode.ID_NOT_EXIST));
        if(user.getUserId() != lecture.getUser().getUserId()){
            throw new InvalidDataAccessApiUsageException("수정 권한이 없습니다.");
        }
        lectureRepository.deleteById(lectureId);
    }

    /**
     * 강의 전체 조회
     */
    @Override
    public Iterable<LectureDto> getLectureByAll() {
        return lectureRepository.findAll();
    }

    /**
     * 카테고리별 강의 조회
     */
    @Override
    public Iterable<LectureDto> getLectureByCategoryId(Long categoryId) throws IdNotExistException{

        Category category = categoryRepository.findById(categoryId).orElseThrow(()->new IdNotExistException("존재하지 않는 카테고리", ResultCode.ID_NOT_EXIST));
        Collection<Lecture> lectures = lectureRepository.findByCategoryId(categoryId);

        return lectures.stream().collect(Collectors.toList());
    }
}