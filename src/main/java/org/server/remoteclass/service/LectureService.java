package org.server.remoteclass.service;

import org.server.remoteclass.dto.LectureFormDto;
import org.server.remoteclass.entity.Lecture;
import org.server.remoteclass.exception.IdNotExistException;

import java.util.List;

public interface LectureService {

    //강의 생성
    Lecture createLecture(LectureFormDto lectureFormDto) throws IdNotExistException;
    //강의 조회
    Lecture getLectureByLectureId(Long lectureId) throws IdNotExistException;
    //강의 수정
    Lecture updateLecture(LectureFormDto lectureFormDto) throws IdNotExistException;
    //강의 삭제
    void deleteLecture(Long lectureId) throws IdNotExistException;

    //전체 강의 조회
    List<Lecture> getLectureByAll();

    //카테고리별 조회
    List<Lecture> getLectureByCategoryId(Long CategoryId) throws IdNotExistException;

}
