package org.server.remoteclass.service;

import org.server.remoteclass.dto.RequestOrderDto;
import org.server.remoteclass.dto.ResponseOrderDto;
import org.server.remoteclass.exception.ForbiddenException;
import org.server.remoteclass.exception.IdNotExistException;

import java.util.List;

public interface OrderService {

    Long createOrder(RequestOrderDto requestOrderDto)throws IdNotExistException;
    void cancelOrder(Long orderId) throws IdNotExistException, ForbiddenException;
    List<ResponseOrderDto> getMyOrdersByUserId() throws IdNotExistException;
    List<ResponseOrderDto> getAllOrdersByAdmin() throws IdNotExistException, ForbiddenException;
    List<ResponseOrderDto> getOrderByUserIdByAdmin(Long userId) throws IdNotExistException, ForbiddenException;
    ResponseOrderDto getOrderByOrderIdByAdmin(Long orderId) throws IdNotExistException, ForbiddenException;
}
