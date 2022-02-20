package org.server.remoteclass.service.student;

import org.server.remoteclass.dto.lecture.ResponseLectureFromStudentDto;
import org.server.remoteclass.dto.student.RequestStudentDto;
import org.server.remoteclass.dto.student.ResponseStudentByLecturerDto;
import org.server.remoteclass.dto.student.StudentDto;
import org.server.remoteclass.exception.ForbiddenException;
import org.server.remoteclass.exception.IdNotExistException;
import org.server.remoteclass.exception.NameDuplicateException;

import java.util.List;

public interface StudentService {

    //수강신청
    StudentDto createStudent(RequestStudentDto requestStudentDto) throws IdNotExistException, NameDuplicateException;
    //수강 취소
    void cancel(Long lectureId) throws IdNotExistException;
    //한 수강생의 수강 강좌 리스트 조회
    List<ResponseLectureFromStudentDto> getLecturesByUserId() throws IdNotExistException, ForbiddenException;
    //강좌별 전체 수강생 조회
    List<ResponseStudentByLecturerDto> getStudentsByLectureId(Long lectureId) throws IdNotExistException, ForbiddenException;
    List<ResponseLectureFromStudentDto> getLecturesByUserIdByAdmin(Long userId) throws IdNotExistException, ForbiddenException;

}