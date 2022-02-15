package org.server.remoteclass.controller;


import io.swagger.annotations.ApiOperation;
import org.server.remoteclass.dto.order.RequestOrderDto;
import org.server.remoteclass.dto.order.ResponseOrderByAdminDto;
import org.server.remoteclass.dto.order.ResponseOrderDto;
import org.server.remoteclass.exception.ForbiddenException;

import org.server.remoteclass.exception.IdNotExistException;
import org.server.remoteclass.service.order.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService){
        this.orderService = orderService;
    }


    @ApiOperation("주문 신청")
    @PostMapping
    public ResponseEntity createOrder(@RequestBody RequestOrderDto requestOrderDto) throws IdNotExistException {
        orderService.createOrder(requestOrderDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @ApiOperation("주문 취소")
    @PutMapping("/{orderId}")
    public ResponseEntity cancelOrder(@PathVariable("orderId") @Valid Long orderId) throws ForbiddenException, IdNotExistException {
        orderService.cancelOrder(orderId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @ApiOperation("사용자 본인 주문 목록 조회")
    @GetMapping("/myList")
    public ResponseEntity<List<ResponseOrderDto>> getMyOrder() throws IdNotExistException {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.getMyOrdersByUserId());
    }
    @ApiOperation("관리자의 주문 전체 목록 조회")
    @GetMapping("/admin/list")
    public ResponseEntity<List<ResponseOrderByAdminDto>> getAllByAdmin() throws IdNotExistException, ForbiddenException {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.getAllOrdersByAdmin());
    }

    @ApiOperation("관리자의 사용자별 목록 조회")
    @GetMapping("/admin/user/{userId}")
    public ResponseEntity<List<ResponseOrderByAdminDto>> getByUserIdByAdmin(@PathVariable("userId") Long userId) throws IdNotExistException, ForbiddenException {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.getOrderByUserIdByAdmin(userId));
    }

    @ApiOperation("관리자의 특정 주문 목록 조회")
    @GetMapping("/admin/order/{orderId}")
    public ResponseEntity<ResponseOrderByAdminDto> getByOrderIdByAdmin(@PathVariable("orderId") Long orderId) throws IdNotExistException, ForbiddenException {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.getOrderByOrderIdByAdmin(orderId));
    }


}
