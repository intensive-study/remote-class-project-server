package org.server.remoteclass.controller;

import io.swagger.annotations.ApiOperation;
import org.server.remoteclass.dto.event.ResponseEventDto;
import org.server.remoteclass.service.event.EventService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

}
