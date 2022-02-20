package org.server.remoteclass.controller;

import io.swagger.annotations.ApiOperation;
import org.server.remoteclass.dto.lecture.RequestModifyLectureDto;
import org.server.remoteclass.dto.lecture.RequestLectureDto;
import org.server.remoteclass.dto.lecture.ResponseLectureDto;
import org.server.remoteclass.exception.ForbiddenException;
import org.server.remoteclass.exception.IdNotExistException;
import org.server.remoteclass.service.lecture.LectureService;
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

    @ApiOperation(value = "강의 생성", notes = "새로운 강의를 생성할 수 있다.")
    @PostMapping
    public ResponseEntity<ResponseLectureDto> createLecture(@RequestBody @Valid RequestLectureDto requestLectureDto) throws IdNotExistException, ForbiddenException {
        lectureService.createLecture(requestLectureDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @ApiOperation(value = "강의 조회", notes = "원하는 강의 번호로 강의를 조회할 수 있다.")
    @GetMapping("/{lectureId}")
    public ResponseEntity<ResponseLectureDto> readLecture(@PathVariable("lectureId") Long lectureId) throws IdNotExistException {
        return ResponseEntity.status(HttpStatus.OK).body(lectureService.getLectureByLectureId(lectureId));
    }

    @ApiOperation(value = "강의 수정", notes = "강의 상세내용을 수정할 수 있다.")
    @PutMapping
    public ResponseEntity<ResponseLectureDto> updateLecture(@RequestBody @Valid RequestModifyLectureDto requestModifyLectureDto) throws IdNotExistException {
        lectureService.updateLecture(requestModifyLectureDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @ApiOperation(value = "강의 삭제", notes = "강의를 삭제할 수 있다.")
    @DeleteMapping("/{lectureId}")
    public ResponseEntity deleteLecture(@PathVariable("lectureId") Long lectureId) throws IdNotExistException {
        lectureService.deleteLecture(lectureId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @ApiOperation(value = "전체 강의 조회", notes = "현재까지 생성된 모든 강의를 조회할 수 있다.")
    @GetMapping("/list")
    public ResponseEntity<List<ResponseLectureDto>> getAllLecture(){
        return ResponseEntity.status(HttpStatus.OK).body(lectureService.getLectureByAll());
    }

    @ApiOperation(value = "카테고리별 강의 조회", notes = "현재까지 생성된 강의를 카테고리별로 조회할 수 있다.")
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<ResponseLectureDto>> getLectureByCategory(@PathVariable("categoryId") Long categoryId) throws IdNotExistException{
        return ResponseEntity.status(HttpStatus.OK).body(lectureService.getLectureByCategoryId(categoryId));
    }
}