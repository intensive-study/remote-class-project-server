package org.server.remoteclass.controller;

import org.server.remoteclass.dto.LectureDto;
import org.server.remoteclass.dto.StudentDto;
import org.server.remoteclass.dto.UserDto;
import org.server.remoteclass.exception.ForbiddenException;
import org.server.remoteclass.exception.IdNotExistException;
import org.server.remoteclass.exception.NameDuplicateException;
import org.server.remoteclass.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/student")
public class StudentController {
    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService){
        this.studentService = studentService;
    }

    //수강 신청 (학생 권한)
    @PostMapping
    public ResponseEntity<StudentDto> applyLecture(@RequestBody @Valid StudentDto studentDto) throws IdNotExistException, NameDuplicateException {
        return ResponseEntity.status(HttpStatus.CREATED).body(studentService.applyLecture(studentDto));
    }

    //수강 취소
    @DeleteMapping("/{lectureId}")
    public ResponseEntity deleteLecture(@PathVariable("lectureId") Long lectureId) throws IdNotExistException {
        studentService.cancel(lectureId);
        return ResponseEntity.status(HttpStatus.OK).body("lecture id: " + lectureId + " 수강 취소");
    }

    //수강하는 강좌 전체 조회(학생 권한)
    @GetMapping("/list")
    public ResponseEntity<List<LectureDto>> getAllLectureByUserId() throws IdNotExistException, ForbiddenException {
        return ResponseEntity.ok(studentService.getLecturesByUserId());
    }

    //수강생 전체 조회 (강의자 권한)
    @GetMapping("/{lectureId}")
    public ResponseEntity<List<UserDto>> getStudentsByLectureId(@PathVariable("lectureId") Long lectureId) throws IdNotExistException, ForbiddenException {
        return ResponseEntity.ok(studentService.getStudentsByLectureId(lectureId));
    }
}