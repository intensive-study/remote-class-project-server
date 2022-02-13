package org.server.remoteclass.controller;

import io.swagger.annotations.ApiOperation;
import org.server.remoteclass.dto.*;
import org.server.remoteclass.entity.OrderLecture;
import org.server.remoteclass.exception.ForbiddenException;
import org.server.remoteclass.exception.IdNotExistException;
import org.server.remoteclass.service.OrderService;
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
    public ResponseEntity createOrder(@RequestBody @Valid OrderDto orderDto) throws IdNotExistException {
        Long orderId= orderService.createOrder(orderDto);
        return new ResponseEntity<Long>(orderId, HttpStatus.OK);
    }

    @ApiOperation("주문 취소")
    @PutMapping("/{orderId}")
    public @ResponseBody ResponseEntity cancelOrder(@PathVariable("orderId") Long orderId) throws ForbiddenException, IdNotExistException {
        orderService.cancelOrder(orderId);
        return new ResponseEntity<Long>(orderId, HttpStatus.OK);
    }


    @ApiOperation("주문 목록 조회")
    @GetMapping("/list")
    public ResponseEntity<List<OrderHistoryDto>> getMyOrder() throws IdNotExistException {
        return ResponseEntity.ok(orderService.getOrdersByUserId());
    }


}
