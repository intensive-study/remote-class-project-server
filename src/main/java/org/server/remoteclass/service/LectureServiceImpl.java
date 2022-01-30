package org.server.remoteclass.service;

import org.modelmapper.ModelMapper;
import org.server.remoteclass.entity.CategoryEntity;
import org.server.remoteclass.entity.LectureEntity;
import org.server.remoteclass.exception.ResultCode;
import org.server.remoteclass.jpa.LectureRepository;
import org.server.remoteclass.vo.RequestLecture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class LectureServiceImpl implements LectureService{

    LectureRepository lectureRepository;
//    CategoryRepository categoryRepository;

    @Autowired
    public LectureServiceImpl(LectureRepository lectureRepository){
        this.lectureRepository = lectureRepository;
//        this.categoryRepository = categoryRepository;
    }


    @Override
    public LectureEntity createLecture(RequestLecture lectureDto) {
        LectureEntity lecture = new ModelMapper().map(lectureDto, LectureEntity.class);
        LectureEntity result = lectureRepository.save(lecture);
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public LectureEntity getLectureByLectureId(Long lectureId) {
        return lectureRepository.findByLectureId(lectureId);
    }

    @Override
    public LectureEntity updateLecture(RequestLecture lectureDto){
        LectureEntity lecture = lectureRepository.findByLectureId(lectureDto.getLectureId());
        //check user and authorities
//        Optional<CategoryEntity> categoryEntity = categoryRepository.findById(lectureDto.getCategoryId());
//        lecture.setCategoryEntity(categoryEntity);
        lecture.setTitle(lectureDto.getTitle());
        lecture.setDescription(lecture.getDescription());
        lecture.setPrice(lectureDto.getPrice());
//        lecture.setStartDate(lectureDto.getStartDate());
//        lecture.setEndDate(lectureDto.getEndDate());

        return lecture;
    }

    @Override
    public void deleteLecture(Long lectureId) {
        LectureEntity lecture = lectureRepository.findByLectureId(lectureId);
        lectureRepository.deleteById(lectureId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LectureEntity> getLectureByAll() {
        return lectureRepository.findAll();
    }
}
