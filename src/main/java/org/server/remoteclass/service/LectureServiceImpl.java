package org.server.remoteclass.service;

import org.modelmapper.ModelMapper;

import org.server.remoteclass.dto.LectureFormDto;
import org.server.remoteclass.entity.Category;
import org.server.remoteclass.entity.Lecture;
import org.server.remoteclass.exception.IdNotExistException;
import org.server.remoteclass.exception.ResultCode;
import org.server.remoteclass.jpa.CategoryRepository;
import org.server.remoteclass.jpa.LectureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LectureServiceImpl implements LectureService{

    LectureRepository lectureRepository;
    CategoryRepository categoryRepository;

    @Autowired
    public LectureServiceImpl(LectureRepository lectureRepository,CategoryRepository categoryRepository){
        this.lectureRepository = lectureRepository;
        this.categoryRepository = categoryRepository;
    }

    /**
     * 강의 생성
     */
    @Override
    public Lecture createLecture(LectureFormDto lectureFormDto) throws IdNotExistException {
        Lecture lecture = new ModelMapper().map(lectureFormDto, Lecture.class);
        Category category = categoryRepository.findById(lectureFormDto.getCategoryId())
                .orElseThrow(() -> new IdNotExistException("카테고리 존재하지 않음", ResultCode.ID_NOT_EXIST));
        lecture.setCategory(category);

        Lecture result = lectureRepository.save(lecture);
        return result;
    }

    /**
     * 특정 강의 조회
     * @param : lectureId
     */
    @Override
    @Transactional(readOnly = true)
    public Lecture getLectureByLectureId(Long lectureId) throws IdNotExistException {
        return lectureRepository.findById(lectureId)
                .orElseThrow(() -> new IdNotExistException("존재하지 않는 강의", ResultCode.ID_NOT_EXIST));
    }


    /**
     * 강의 수정
     */
    @Override
    @Transactional
    public Lecture updateLecture(LectureFormDto lectureDto) throws IdNotExistException{
        //check user and authorities

        Lecture lecture = lectureRepository.findById(lectureDto.getLectureId())
                .orElseThrow(() -> new IdNotExistException("존재하지 않는 강의", ResultCode.ID_NOT_EXIST));
        Category category = categoryRepository.findById(lectureDto.getCategoryId())
                .orElseThrow(() -> new IdNotExistException("존재하지 않는 카테고리", ResultCode.ID_NOT_EXIST));;

        lecture.setTitle(lectureDto.getTitle());
        lecture.setDescription(lectureDto.getDescription());
        lecture.setPrice(lectureDto.getPrice());
        lecture.setStartDate(lectureDto.getStartDate());
        lecture.setEndDate(lectureDto.getEndDate());
        lecture.setCategory(category);

        return lecture;
    }

    /**
     * 강의 삭제
     * @param lectureId
     */

    @Override
    @Transactional
    public void deleteLecture(Long lectureId) throws IdNotExistException{
        //check user and check if user has delete authority
        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new IdNotExistException("존재하지 않는 강의", ResultCode.ID_NOT_EXIST));
        lectureRepository.deleteById(lectureId);
    }

    /**
     * 강의 전체 조회
     */
    @Override
    @Transactional(readOnly = true)
    public List<Lecture> getLectureByAll() {
        return lectureRepository.findAll();
    }

    /**
     * 카테고리별 강의 조회
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public List<Lecture> getLectureByCategoryId(Long categoryId) throws IdNotExistException{
        Collection<Lecture> lecture;
        //categoryId가 0이하일 경우, 존재하지 않는다
        if(categoryId > 0){
            lecture = lectureRepository.findByCategoryId(categoryId);
        }
        else{
            throw new IdNotExistException("존재하지 않는 카테고리", ResultCode.ID_NOT_EXIST);
        }
        if(lecture.isEmpty()){
            throw new IdNotExistException("존재하지 않는 강의", ResultCode.ID_NOT_EXIST);
        }
        return lecture.stream().collect(Collectors.toList());
    }
}