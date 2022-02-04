package org.server.remoteclass.service;

import org.server.remoteclass.dto.LectureDto;
import org.server.remoteclass.dto.StudentDto;
import org.server.remoteclass.dto.StudentFormDto;
import org.server.remoteclass.entity.Student;
import org.server.remoteclass.exception.IdNotExistException;

import java.util.List;

public interface StudentService {

//    //수강신청
    StudentDto applyLecture(StudentFormDto studentFormDto) throws IdNotExistException;
    //강좌별 전체 수강생 조회
    Iterable<StudentDto> getStudentsByLectureId(Long lectureId) throws IdNotExistException;
    //한 수강생의 수강 강좌 리스트 조회
    Iterable<LectureDto> getLecturesByUserId() throws IdNotExistException;

}