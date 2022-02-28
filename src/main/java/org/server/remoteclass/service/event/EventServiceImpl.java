package org.server.remoteclass.service.event;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.server.remoteclass.dto.coupon.RequestCouponDto;
import org.server.remoteclass.dto.coupon.ResponseCouponDto;
import org.server.remoteclass.dto.event.RequestEventDto;
import org.server.remoteclass.dto.event.RequestUpdateEventDto;
import org.server.remoteclass.dto.event.ResponseEventDto;
import org.server.remoteclass.dto.fixDiscountCoupon.RequestFixDiscountCouponDto;
import org.server.remoteclass.dto.fixDiscountCoupon.ResponseFixDiscountCouponDto;
import org.server.remoteclass.dto.rateDiscountCoupon.RequestRateDiscountCouponDto;
import org.server.remoteclass.dto.rateDiscountCoupon.ResponseRateDiscountCouponDto;
import org.server.remoteclass.entity.Coupon;
import org.server.remoteclass.entity.Event;
import org.server.remoteclass.exception.BadRequestArgumentException;
import org.server.remoteclass.exception.ErrorCode;
import org.server.remoteclass.exception.IdNotExistException;
import org.server.remoteclass.jpa.CouponRepository;
import org.server.remoteclass.jpa.EventRepository;
import org.server.remoteclass.jpa.FixDiscountCouponRepository;
import org.server.remoteclass.service.coupon.CouponService;
import org.server.remoteclass.service.fixDiscountCoupon.FixDiscountCouponService;
import org.server.remoteclass.service.rateDiscountCoupon.RateDiscountCouponService;
import org.server.remoteclass.util.BeanConfiguration;
import org.springframework.scheduling.annotation.Scheduled;
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
    private final CouponRepository couponRepository;
    private final CouponService couponService;
    private final FixDiscountCouponService fixDiscountCouponService;
    private final RateDiscountCouponService rateDiscountCouponService;
    private final ModelMapper modelMapper;

    public EventServiceImpl(EventRepository eventRepository, CouponService couponService, BeanConfiguration beanConfiguration,
                            FixDiscountCouponService fixDiscountCouponService, RateDiscountCouponService rateDiscountCouponService,
                            CouponRepository couponRepository){
        this.eventRepository = eventRepository;
        this.couponService = couponService;
        this.modelMapper = beanConfiguration.modelMapper();
        this.fixDiscountCouponService = fixDiscountCouponService;
        this.rateDiscountCouponService = rateDiscountCouponService;
        this.couponRepository = couponRepository;
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
        if(requestEventDto.getDiscountPrice() != null){
            RequestFixDiscountCouponDto requestFixDiscountCouponDto = modelMapper.map(requestEventDto, RequestFixDiscountCouponDto.class);
            requestFixDiscountCouponDto.setTitle(requestEventDto.getEventTitle());
            ResponseFixDiscountCouponDto responseFixDiscountCouponDto = fixDiscountCouponService.createFixDiscountCoupon(requestFixDiscountCouponDto);
            Event event = modelMapper.map(requestEventDto, Event.class);
            Coupon coupon = (Coupon) couponRepository.findByCouponId(responseFixDiscountCouponDto.getCouponId()).orElse(null);
            if(coupon == null){
                throw new IdNotExistException("해당하는 쿠폰이 없습니다.", ErrorCode.ID_NOT_EXIST);
            }
            event.setCoupon(coupon);
            eventRepository.save(event);
        }

        else if(requestEventDto.getDiscountRate() != null){
            RequestRateDiscountCouponDto requestRateDiscountCouponDto = modelMapper.map(requestEventDto, RequestRateDiscountCouponDto.class);
            requestRateDiscountCouponDto.setTitle(requestEventDto.getEventTitle());
            ResponseRateDiscountCouponDto responseRateDiscountCouponDto = rateDiscountCouponService.createRateDiscountCoupon(requestRateDiscountCouponDto);
            Event event = modelMapper.map(requestEventDto, Event.class);
            Coupon coupon = (Coupon) couponRepository.findByCouponId(responseRateDiscountCouponDto.getCouponId()).orElse(null);
            if(coupon == null){
                throw new IdNotExistException("해당하는 쿠폰이 없습니다.", ErrorCode.ID_NOT_EXIST);
            }
            event.setCoupon(coupon);
            eventRepository.save(event);
        }
        else{
            throw new BadRequestArgumentException("요청 인자가 올바르지 않습니다.", ErrorCode.BAD_REQUEST_ARGUMENT);
        }
    }

    @Override
    @Transactional
    public void updateEvent(RequestUpdateEventDto requestUpdateEventDto) {
        Event event = eventRepository.findByEventId(requestUpdateEventDto.getEventId())
                .orElseThrow(() -> new IdNotExistException("존재하는 이벤트가 없습니다.", ErrorCode.ID_NOT_EXIST));

        event.setTitle(requestUpdateEventDto.getTitle());
        event.setEventStartDate(requestUpdateEventDto.getEventStartDate());
        event.setEventEndDate(requestUpdateEventDto.getEventEndDate());
        event.getCoupon().setCouponValidDays(requestUpdateEventDto.getCouponValidDays());
    }

    @Override
    @Transactional
    public void deleteEvent(Long eventId) {
        eventRepository.findByEventId(eventId).orElseThrow(() -> new IdNotExistException("존재하는 이벤트가 없습니다", ErrorCode.ID_NOT_EXIST));
        eventRepository.deleteByEventId(eventId);
    }

    @Override
    public void quitEvent(Long eventId) {
        Event event = eventRepository.findByEventId(eventId).orElseThrow(() -> new IdNotExistException("존재하는 이벤트가 없습니다", ErrorCode.ID_NOT_EXIST));
        event.getCoupon().setCouponValid(false);
    }

    @Transactional
    @Override
    public void quitEventByScheduler(){
        // 이벤트에서 쿠폰이 생성되므로, 이벤트와 연결된 쿠폰을 가져와서 현재 시간 - 쿠폰 시간 < 0인 경우 종료시킴
        // 스케쥴러 활용하기
        List<Event> eventList = eventRepository.findAll();
        for (Event event : eventList) {
            if((LocalDateTime.now()).isAfter(event.getEventEndDate())){
                // 이벤트와 연관된 쿠폰 비활성화하기
                event.getCoupon().setCouponValid(false);
            }
        }
    }

}
