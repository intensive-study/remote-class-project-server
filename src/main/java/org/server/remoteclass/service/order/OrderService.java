package org.server.remoteclass.service.order;

import org.server.remoteclass.dto.order.OrderDto;
import org.server.remoteclass.dto.order.OrderFormDto;
import org.server.remoteclass.entity.OrderLecture;
import org.server.remoteclass.exception.IdNotExistException;

import java.util.List;

public interface OrderService {

//    List<OrderLectureDto> createOrderLecture(OrderLectureDto orderLectureDto) throws IdNotExistException;
    //주문  신청
    OrderDto createOrder(OrderFormDto orderFormDto,List<OrderLecture> orderLectures) throws IdNotExistException;
    //주문 취소
    OrderDto cancelOrder(Long orderID) throws IdNotExistException;

    //사용자 아이디로 해당 주문목록 조회
    List<OrderDto> getOrdersByUserId() throws IdNotExistException;

    //주문의 가격 합
    Double getSumOrdersByOrderId() throws IdNotExistException;
}