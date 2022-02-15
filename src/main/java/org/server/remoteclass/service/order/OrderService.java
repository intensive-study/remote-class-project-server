package org.server.remoteclass.service.order;

import org.server.remoteclass.dto.order.RequestOrderDto;
import org.server.remoteclass.dto.order.ResponseOrderByAdminDto;
import org.server.remoteclass.dto.order.ResponseOrderDto;
import org.server.remoteclass.exception.ForbiddenException;

import org.server.remoteclass.exception.IdNotExistException;

import java.util.List;

public interface OrderService {

    Long createOrder(RequestOrderDto requestOrderDto)throws IdNotExistException;
    void cancelOrder(Long orderId) throws IdNotExistException, ForbiddenException;
    List<ResponseOrderDto> getMyOrdersByUserId() throws IdNotExistException;
    List<ResponseOrderByAdminDto> getAllOrdersByAdmin() throws IdNotExistException, ForbiddenException;
    List<ResponseOrderByAdminDto> getOrderByUserIdByAdmin(Long userId) throws IdNotExistException, ForbiddenException;
    ResponseOrderByAdminDto getOrderByOrderIdByAdmin(Long orderId) throws IdNotExistException, ForbiddenException;

}
