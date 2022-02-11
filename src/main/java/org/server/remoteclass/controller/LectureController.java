package org.server.remoteclass.controller;

import org.server.remoteclass.dto.LectureDto;
import org.server.remoteclass.dto.LectureFormDto;
import org.server.remoteclass.exception.ForbiddenException;
import org.server.remoteclass.exception.IdNotExistException;
import org.server.remoteclass.service.LectureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/lectures")
public class LectureController {

    private final LectureService lectureService;

    @Autowired
    public LectureController(LectureService lectureService){
        this.lectureService = lectureService;
    }

    //강의 생성
    @PostMapping
    public ResponseEntity<LectureDto> createLecture(@RequestBody @Valid LectureFormDto lectureFormDto) throws IdNotExistException, ForbiddenException {
        return ResponseEntity.status(HttpStatus.CREATED).body(lectureService.createLecture(lectureFormDto));
    }
    //강의 조회
    @GetMapping("/{lectureId}")
    public ResponseEntity<LectureDto> readLecture(@PathVariable("lectureId") Long lectureId) throws IdNotExistException {
        return ResponseEntity.status(HttpStatus.OK).body(lectureService.getLectureByLectureId(lectureId));
    }

    //강의 수정
    @PutMapping
    public ResponseEntity<LectureDto> updateLecture(@RequestBody @Valid LectureFormDto lectureFormDto) throws IdNotExistException {
        return ResponseEntity.status(HttpStatus.CREATED).body(lectureService.updateLecture(lectureFormDto));
    }

    //강의 삭제
    @DeleteMapping("/{lectureId}")
    public ResponseEntity deleteLecture(@PathVariable("lectureId") Long lectureId) throws IdNotExistException {
        lectureService.deleteLecture(lectureId);
        return ResponseEntity.status(HttpStatus.OK).body("lecture id: " + lectureId + " 삭제완료");
    }

    // 강의 전체 목록 조회
    @GetMapping("/list")
    public ResponseEntity<List<LectureDto>> getAllLecture(){
        return ResponseEntity.ok(lectureService.getLectureByAll());
    }

    //카테고리별 강의 목록 조회
    @GetMapping("/list/{categoryId}")
    public ResponseEntity<List<LectureDto>> getLectureByCategory(@PathVariable("categoryId") Long categoryId) throws IdNotExistException{
        return ResponseEntity.ok(lectureService.getLectureByCategoryId(categoryId));
    }
}