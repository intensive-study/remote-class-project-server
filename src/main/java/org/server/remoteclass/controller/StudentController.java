package org.server.remoteclass.controller;

import io.swagger.annotations.ApiOperation;

import org.server.remoteclass.dto.lecture.ResponseLectureFromStudentDto;
import org.server.remoteclass.dto.student.ResponseStudentByLecturerDto;
import org.server.remoteclass.service.student.StudentService;
import org.server.remoteclass.util.AccessVerification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@PreAuthorize("hasRole('ROLE_USER')")
@RequestMapping("/students")
public class StudentController {
    private final StudentService studentService;
    private final AccessVerification accessVerification;

    @Autowired
    public StudentController(StudentService studentService, AccessVerification accessVerification){
        this.studentService = studentService;
        this.accessVerification = accessVerification;
    }

    @ApiOperation(value = "전체 수강 강좌 조회", notes = "학생 본인이 현재까지 수강 신청한 모든 강의를 조회할 수 있다.")
    @GetMapping
    public ResponseEntity<List<ResponseLectureFromStudentDto>> getAllLecturesByUserId() {
        return ResponseEntity.status(HttpStatus.OK).body(studentService.getLecturesByUserId());
    }


    @ApiOperation(value = "전체 수강생 조회(강사 권한)", notes = "강의자가 현재까지 수강신청한 모든 수강생을 조회할 수 있다.")
    @PreAuthorize("@accessVerification.hasAccessOnlyLecturer()")
    @GetMapping("/{lectureId}")
    public ResponseEntity<List<ResponseStudentByLecturerDto>> getStudentsByLectureId(@PathVariable("lectureId") Long lectureId) {
        return ResponseEntity.status(HttpStatus.OK).body(studentService.getStudentsByLectureId(lectureId));
    }
}