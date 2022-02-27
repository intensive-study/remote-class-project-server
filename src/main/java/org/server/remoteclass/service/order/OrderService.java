package org.server.remoteclass.service.order;

import org.server.remoteclass.dto.order.RequestOrderDto;
import org.server.remoteclass.dto.order.RequestOrderFromCartDto;
import org.server.remoteclass.dto.order.ResponseOrderByAdminDto;
import org.server.remoteclass.dto.order.ResponseOrderDto;

import java.util.List;

public interface OrderService {

    void createOrder(RequestOrderDto requestOrderDto);
    void createOrderFromCart(RequestOrderFromCartDto requestOrderFromCartDto);
    void cancelOrder(Long orderId);
    List<ResponseOrderDto> getMyOrdersByUserId();
    List<ResponseOrderByAdminDto> getAllOrdersByAdmin();
    List<ResponseOrderByAdminDto> getOrderByUserIdByAdmin(Long userId);
    ResponseOrderByAdminDto getOrderByOrderIdByAdmin(Long orderId);

}
