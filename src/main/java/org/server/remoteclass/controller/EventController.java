package org.server.remoteclass.controller;

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

    @GetMapping
    public ResponseEntity<List<ResponseEventDto>> getAllEvents(){
        return ResponseEntity.status(HttpStatus.OK).body(eventService.getAllEvents());
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<ResponseEventDto> getEvent(@PathVariable("eventId") Long eventId){
        return ResponseEntity.status(HttpStatus.OK).body(eventService.getEvent(eventId));
    }

    @PostMapping
    public ResponseEntity createEvent(@RequestBody @Valid RequestEventDto requestEventDto){
        eventService.createEvent(requestEventDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{eventId}")
    public ResponseEntity deleteEvent(@PathVariable("eventId") Long eventId){
        eventService.deleteEvent(eventId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
