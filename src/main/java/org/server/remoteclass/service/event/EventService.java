package org.server.remoteclass.service.event;

import org.server.remoteclass.dto.event.RequestEventDto;
import org.server.remoteclass.dto.event.RequestUpdateEventDto;
import org.server.remoteclass.dto.event.ResponseEventDto;

import java.util.List;

public interface EventService {

    // 이벤트 전체 조회
    List<ResponseEventDto> getAllEvents();
    // 이벤트 개별 상세 조회
    ResponseEventDto getEvent(Long eventId);
    // 이벤트 생성
    void createEvent(RequestEventDto requestEventDto);
    // 이벤트 수정
    void updateEvent(RequestUpdateEventDto requestUpdateEventDto);
    // 이벤트 삭제
    void deleteEvent(Long eventId);
    // 이벤트 종료
    void quitEvent(Long eventId);
    // 스케줄러로 이벤트 순회하며 종료시키기(미구현)
    void quitEventByScheduler();
}
