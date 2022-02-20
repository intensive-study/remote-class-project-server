package org.server.remoteclass.service.lecture;

import org.server.remoteclass.dto.lecture.RequestModifyLectureDto;
import org.server.remoteclass.dto.lecture.RequestLectureDto;
import org.server.remoteclass.dto.lecture.ResponseLectureDto;
import org.server.remoteclass.exception.ForbiddenException;
import org.server.remoteclass.exception.IdNotExistException;

import java.util.List;

public interface LectureService {

    //강의 생성
    ResponseLectureDto createLecture(RequestLectureDto requestLectureDto) throws IdNotExistException, ForbiddenException;
    //강의 조회
    ResponseLectureDto getLectureByLectureId(Long lectureId) throws IdNotExistException;
    //강의 수정
    ResponseLectureDto updateLecture(RequestModifyLectureDto requestModifyLectureDto) throws IdNotExistException;
    //강의 삭제
    void deleteLecture(Long lectureId) throws IdNotExistException;

    //전체 강의 조회
    List<ResponseLectureDto> getLectureByAll();

    //카테고리별 조회
    List<ResponseLectureDto> getLectureByCategoryId(Long CategoryId) throws IdNotExistException;

}
