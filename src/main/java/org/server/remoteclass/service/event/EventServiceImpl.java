package org.server.remoteclass.service.event;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.server.remoteclass.dto.coupon.RequestCouponDto;
import org.server.remoteclass.dto.coupon.ResponseCouponDto;
import org.server.remoteclass.dto.event.RequestEventDto;
import org.server.remoteclass.dto.event.ResponseEventDto;
import org.server.remoteclass.entity.Coupon;
import org.server.remoteclass.entity.Event;
import org.server.remoteclass.exception.ErrorCode;
import org.server.remoteclass.exception.IdNotExistException;
import org.server.remoteclass.jpa.EventRepository;
import org.server.remoteclass.service.coupon.CouponService;
import org.server.remoteclass.util.BeanConfiguration;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional(readOnly = true)
public class EventServiceImpl implements EventService{

    private final EventRepository eventRepository;
    private final CouponService couponService;
    private final ModelMapper modelMapper;

    public EventServiceImpl(EventRepository eventRepository, CouponService couponService, BeanConfiguration beanConfiguration){
        this.eventRepository = eventRepository;
        this.couponService = couponService;
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

    @Transactional
    @Override
    public void createEvent(RequestEventDto requestEventDto) {
        RequestCouponDto requestCouponDto = new RequestCouponDto();
        //이벤트와 같이 쿠폰도 생성해야 하므로 쿠폰 생성 시 필요한 Dto를 만들고, 데이터를 집어넣습니다.
        requestCouponDto.setCouponValidDays(requestEventDto.getCouponValidDays());
        ResponseCouponDto responseCouponDto = couponService.createCoupon(requestCouponDto);
        Event event = modelMapper.map(requestEventDto, Event.class);
        //responseCouponDto로 해당하는 Coupon Entity 찾아서 넣는 방법도 고려중입니다.(사실 이게 더 좋아 보여요)
        Coupon coupon = modelMapper.map(responseCouponDto, Coupon.class);
        event.setCoupon(coupon);
        event.setTitle(requestEventDto.getTitle());
        event.setEventStartDate(requestEventDto.getEventStartDate());
        event.setEventEndDate(requestEventDto.getEventEndDate());
        eventRepository.save(event);
    }

    @Override
    public void updateEvent(RequestEventDto requestEventDto) {

    }

    @Override
    public void deleteEvent(Long eventId) {
        eventRepository.findByEventId(eventId).orElseThrow(() -> new IdNotExistException("해당하는 이벤트가 없습니다", ErrorCode.ID_NOT_EXIST));
        eventRepository.deleteByEventId(eventId);
    }


    // 현재 미구현 상태입니다.
    @Transactional
    @Override
    public void quitEvent(){
        // 이벤트에서 쿠폰이 생성되므로, 이벤트와 연결된 쿠폰을 가져와서 현재 시간 - 쿠폰 시간 < 0인 경우 종료시킴
        // 스케쥴러 활용하기
        List<Event> eventList = eventRepository.findAll();
        for (Event event : eventList) {
            if((LocalDateTime.now()).isAfter(event.getEventEndDate())){
                // 이벤트와 연관된 쿠폰 비활성화하기
            }
        }
    }

}
