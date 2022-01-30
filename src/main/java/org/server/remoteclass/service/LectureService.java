package org.server.remoteclass.service;

import org.server.remoteclass.entity.LectureEntity;
import org.server.remoteclass.vo.RequestLecture;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface LectureService {

    //강의 생성
    LectureEntity createLecture(RequestLecture lectureDto);
    //강의 조회
    LectureEntity getLectureByLectureId(Long lectureId);
    //강의 수정
    LectureEntity updateLecture(RequestLecture lectureDto);
    //강의 삭제
    void deleteLecture(Long lectureId);

    //전체 강의 조회
    List<LectureEntity> getLectureByAll();
    //카테고리별 조회
//    @Transactional(readOnly = true)
//    List<LectureEntity> getLectureByCategoryId(Long CategoryId) throws IdNotExistException;

}
