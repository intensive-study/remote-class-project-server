package org.server.remoteclass.controller;

import io.swagger.annotations.ApiOperation;
import org.server.remoteclass.dto.event.RequestEventDto;
import org.server.remoteclass.dto.event.ResponseEventDto;
import org.server.remoteclass.service.EventService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/events")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService){
        this.eventService = eventService;
    }

    @ApiOperation(value = "모든 이벤트 조회")
    @GetMapping
    public ResponseEntity<List<ResponseEventDto>> getAllEvents(){
        return ResponseEntity.status(HttpStatus.OK).body(eventService.getAllEvents());
    }

    @ApiOperation(value = "이벤트 번호로 이벤트 조회")
    @GetMapping("/{eventId}")
    public ResponseEntity<ResponseEventDto> getEvent(@PathVariable("eventId") Long eventId){
        return ResponseEntity.status(HttpStatus.OK).body(eventService.getEvent(eventId));
    }

    @ApiOperation(value = "이벤트 생성", notes = "이벤트 생성과 동시에 이벤트와 연계된 쿠폰을 생성한다.")
    @PostMapping
    public ResponseEntity createEvent(@RequestBody @Valid RequestEventDto requestEventDto){
        eventService.createEvent(requestEventDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @ApiOperation(value = "이벤트 삭제", notes = "이벤트 번호를 파라미터로 넘겨 해당하는 이벤트를 삭제한다.")
    @DeleteMapping("/{eventId}")
    public ResponseEntity deleteEvent(@PathVariable("eventId") Long eventId){
        eventService.deleteEvent(eventId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
