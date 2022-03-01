package org.server.remoteclass.service.lecture;

import org.server.remoteclass.dto.lecture.RequestModifyLectureDto;
import org.server.remoteclass.dto.lecture.RequestLectureDto;
import org.server.remoteclass.dto.lecture.ResponseLectureDto;

import java.util.List;

public interface LectureService {
    void createLecture(RequestLectureDto requestLectureDto);
    ResponseLectureDto getLectureByLectureId(Long lectureId);
    void updateLecture(RequestModifyLectureDto requestModifyLectureDto);
    void deleteLecture(Long lectureId);
    List<ResponseLectureDto> getAllLectures();
    List<ResponseLectureDto> getLecturesByCategoryId(Long CategoryId);
    Boolean checkIfUserIsLecturerInLecture(Long lectureId, Long userId);
}
