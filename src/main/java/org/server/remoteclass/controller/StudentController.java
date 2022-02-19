package org.server.remoteclass.controller;

import io.swagger.annotations.ApiOperation;

import org.server.remoteclass.dto.lecture.ResponseLectureDto;
import org.server.remoteclass.dto.student.RequestStudentDto;
import org.server.remoteclass.dto.student.ResponseStudentByLecturerDto;
import org.server.remoteclass.dto.student.StudentDto;
import org.server.remoteclass.exception.ForbiddenException;
import org.server.remoteclass.exception.IdNotExistException;
import org.server.remoteclass.exception.EmailDuplicateException;
import org.server.remoteclass.service.student.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@PreAuthorize("hasRole('ROLE_USER')")
@RequestMapping("/students")
public class StudentController {
    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService){
        this.studentService = studentService;
    }

    @ApiOperation(value = "수강 신청", notes = "학생이 수강 신청할 수 있다.")
    @PostMapping
    public ResponseEntity<StudentDto> createStudent(@RequestBody @Valid RequestStudentDto requestStudentDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(studentService.createStudent(requestStudentDto));
    }

    @ApiOperation(value = "수강 취소", notes = "학생이 신청했던 강의를 취소할 수 있다.")
    @DeleteMapping("/{lectureId}")
    public ResponseEntity deleteStudent(@PathVariable("lectureId") Long lectureId) {
        studentService.cancel(lectureId);
        return ResponseEntity.status(HttpStatus.OK).body("lecture id: " + lectureId + " 수강 취소");
    }

    // 이것도 list가 없어도 될 것 같은데, list가 있어야 와닿는 면도 있어서 일단 보류하겠습니다.
    @ApiOperation(value = "전체 수강 강좌 조회", notes = "학생 본인이 현재까지 수강 신청한 모든 강의를 조회할 수 있다.")
    @GetMapping("/list")
    public ResponseEntity<List<ResponseLectureDto>> getAllLectureByUserId() {
        return ResponseEntity.ok(studentService.getLecturesByUserId());
    }

    //수강생 전체 조회 (강의자 권한) -> 강사 권한으로 바꿔야 되겠죠..?
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ApiOperation(value = "전체 수강생 조회", notes = "강의자가 현재까지 수강신청한 모든 수강생을 조회할 수 있다.")
    @GetMapping("/{lectureId}")
    public ResponseEntity<List<ResponseStudentByLecturerDto>> getStudentsByLectureId(@PathVariable("lectureId") Long lectureId) {
        return ResponseEntity.ok(studentService.getStudentsByLectureId(lectureId));
    }
}