package org.server.remoteclass.service;

import org.server.remoteclass.dto.OrderDto;
import org.server.remoteclass.dto.OrderHistoryDto;
import org.server.remoteclass.exception.ForbiddenException;
import org.server.remoteclass.exception.IdNotExistException;

import java.util.List;

public interface OrderService {

    Long createOrder(OrderDto orderDto)throws IdNotExistException;
    void cancelOrder(Long orderId) throws IdNotExistException, ForbiddenException;

        //사용자 아이디로 해당 주문목록 조회
    List<OrderHistoryDto> getOrdersByUserId() throws IdNotExistException;
//    Long createOrderList(List<OrderDto> orderDtoList) throws IdNotExistException;
//    OrderDto createOrder(OrderFormDto orderFormDto,List<OrderLecture> orderLectures) throws IdNotExistException;
//    //주문 취소
//    OrderDto cancelOrder(Long orderID) throws IdNotExistException;
//
//    //사용자 아이디로 해당 주문목록 조회
//    List<OrderDto> getOrdersByUserId() throws IdNotExistException;
//
//    //주문의 가격 합
//    Double getSumOrdersByOrderId() throws IdNotExistException;
}
