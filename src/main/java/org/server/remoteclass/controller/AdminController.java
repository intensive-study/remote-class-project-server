package org.server.remoteclass.controller;

import org.server.remoteclass.dto.StudentDto;
import org.server.remoteclass.exception.IdNotExistException;
import org.server.remoteclass.jpa.UserRepository;
import org.server.remoteclass.service.StudentService;
import org.server.remoteclass.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final StudentService studentService;

    @Autowired
    public AdminController(StudentService studentService){
        this.studentService = studentService;
    }

    //수강생 전체 조회 /강의자 권한
    @GetMapping("/{lectureId}/list")
    public ResponseEntity<Iterable<StudentDto>> getStudentsByLectureId(@PathVariable("lectureId") Long lectureId) throws IdNotExistException {
        return ResponseEntity.ok(studentService.getStudentsByLectureId(lectureId));
    }

}
