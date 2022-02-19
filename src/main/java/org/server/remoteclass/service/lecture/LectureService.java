package org.server.remoteclass.service.lecture;

import org.server.remoteclass.dto.lecture.LectureDto;
import org.server.remoteclass.exception.ForbiddenException;
import org.server.remoteclass.exception.IdNotExistException;

import java.util.List;

public interface LectureService {

    //강의 생성
    LectureDto createLecture(LectureDto lectureDto) throws IdNotExistException, ForbiddenException;
    //강의 조회
    LectureDto getLectureByLectureId(Long lectureId) throws IdNotExistException;
    //강의 수정
    LectureDto updateLecture(LectureDto lectureDto) throws IdNotExistException;
    //강의 삭제
    void deleteLecture(Long lectureId) throws IdNotExistException;

    //전체 강의 조회
    List<LectureDto> getLectureByAll();

    //카테고리별 조회
    List<LectureDto> getLectureByCategoryId(Long CategoryId) throws IdNotExistException;

}
