package org.server.remoteclass.service;

import org.server.remoteclass.dto.LectureDto;
import org.server.remoteclass.dto.StudentDto;
import org.server.remoteclass.dto.UserDto;
import org.server.remoteclass.exception.ForbiddenException;
import org.server.remoteclass.exception.IdNotExistException;
import org.server.remoteclass.exception.NameDuplicateException;

import java.util.List;

public interface StudentService {

    //수강신청
    StudentDto applyLecture(StudentDto studentDto) throws IdNotExistException, NameDuplicateException;
    //수강 취소
    void cancel(Long lectureId) throws IdNotExistException;
    //한 수강생의 수강 강좌 리스트 조회
    List<LectureDto> getLecturesByUserId() throws IdNotExistException, ForbiddenException;
    //강좌별 전체 수강생 조회
    List<UserDto> getStudentsByLectureId(Long lectureId) throws IdNotExistException, ForbiddenException;


}