package org.server.remoteclass.service;

import org.modelmapper.ModelMapper;
import org.server.remoteclass.dto.coupon.IssuedCouponDto;
import org.server.remoteclass.dto.event.RequestEventDto;
import org.server.remoteclass.dto.event.ResponseEventDto;
import org.server.remoteclass.entity.Event;
import org.server.remoteclass.entity.IssuedCoupon;
import org.server.remoteclass.jpa.EventRepository;
import org.server.remoteclass.util.BeanConfiguration;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EventServiceImpl implements EventService{

    private final EventRepository eventRepository;
    private final ModelMapper modelMapper;

    public EventServiceImpl(EventRepository eventRepository, BeanConfiguration beanConfiguration){
        this.eventRepository = eventRepository;
        this.modelMapper = beanConfiguration.modelMapper();
    }

    @Override
    public List<ResponseEventDto> getAllEvents() {

        List<Event> eventList = eventRepository.findAll();
        return eventList.stream()
                .map(event -> ResponseEventDto.from(event)).collect(Collectors.toList());
    }

    @Override
    public ResponseEventDto getEvent(Long eventId) {
        return ResponseEventDto.from(eventRepository.findByEventId(eventId).orElse(null));
    }

    @Override
    public void createEvent(RequestEventDto requestEventDto) {

    }

    @Override
    public void deleteEvent() {

    }
}
