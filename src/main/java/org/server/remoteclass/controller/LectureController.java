package org.server.remoteclass.controller;

import lombok.RequiredArgsConstructor;
import org.server.remoteclass.dto.LectureDto;
import org.server.remoteclass.entity.LectureEntity;
import org.server.remoteclass.service.LectureService;
import org.server.remoteclass.vo.RequestLecture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/lecture")
public class LectureController {

    private final LectureService lectureService;

    @Autowired
    public LectureController(LectureService lectureService){
        this.lectureService = lectureService;
    }

    @PostMapping("/create")
    public ResponseEntity createLecture(@RequestBody @Valid RequestLecture requestLecture){
        LectureEntity lecture = lectureService.createLecture(requestLecture);
        LectureDto lectureDto = new LectureDto(lecture);
        return ResponseEntity.status(HttpStatus.CREATED).body(lectureDto);
    }

    @GetMapping("/read/{lectureId}")
    public ResponseEntity<LectureDto> readLecture(@PathVariable("lectureId") Long lectureId){
        LectureEntity lectureEntity = lectureService.getLectureByLectureId(lectureId);
        LectureDto lectureDto = new LectureDto(lectureEntity);
        return ResponseEntity.status(HttpStatus.OK).body(lectureDto);
    }


    @PutMapping("/update/{lectureId}")
    public ResponseEntity updateLecture(@RequestBody @Valid @PathVariable("lectureId") RequestLecture requestLecture){
        LectureEntity lecture = lectureService.updateLecture(requestLecture);
        LectureDto lectureDto = new LectureDto(lecture);
        System.out.println(requestLecture.getLectureId());
        return ResponseEntity.status(HttpStatus.CREATED).body(lectureDto);
    }

    @DeleteMapping("/delete/{lectureId}")
    public ResponseEntity deleteLecture(@PathVariable("lectureId") Long lectureId){
        lectureService.deleteLecture(lectureId);
        return ResponseEntity.status(HttpStatus.OK).body("lecture id: " + lectureId + " 삭제완료");
    }

    @GetMapping("/list")
    public List<LectureDto> getAllLecture(){
        return this.lectureService.getLectureByAll().stream()
                .map(LectureDto::new).collect(Collectors.toList());
    }


}
