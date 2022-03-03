package org.server.remoteclass.service.student;

import org.server.remoteclass.dto.lecture.ResponseLectureFromStudentDto;
import org.server.remoteclass.dto.student.ResponseStudentByLecturerDto;

import java.util.List;

public interface StudentService {
    List<ResponseLectureFromStudentDto> getLecturesByUserId();
    List<ResponseStudentByLecturerDto> getStudentsByLectureId(Long lectureId);
    List<ResponseLectureFromStudentDto> getLecturesByUserIdByAdmin(Long userId);
    List<ResponseStudentByLecturerDto> getStudentsByLectureIdByAdmin(Long lectureId);
    Boolean checkIfUserIsStudentInLecture(Long lectureId, Long userId);
}