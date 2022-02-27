package org.server.remoteclass.service.student;

import org.server.remoteclass.dto.lecture.ResponseLectureFromStudentDto;
import org.server.remoteclass.dto.student.RequestStudentDto;
import org.server.remoteclass.dto.student.ResponseStudentByLecturerDto;

import java.util.List;

public interface StudentService {


    //한 수강생의 수강 강좌 리스트 조회
    List<ResponseLectureFromStudentDto> getLecturesByUserId();
    //강좌별 전체 수강생 조회
    List<ResponseStudentByLecturerDto> getStudentsByLectureId(Long lectureId);
    //수강생의 수강 강의 조회
    List<ResponseLectureFromStudentDto> getLecturesByUserIdByAdmin(Long userId);
    List<ResponseStudentByLecturerDto> getStudentsByLectureIdByAdmin(Long lectureId);

}