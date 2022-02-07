package org.server.remoteclass.controller;

import org.server.remoteclass.dto.LectureDto;
import org.server.remoteclass.dto.OrderDto;
import org.server.remoteclass.dto.OrderFormDto;
import org.server.remoteclass.dto.OrderLectureDto;
import org.server.remoteclass.entity.OrderLecture;
import org.server.remoteclass.exception.IdNotExistException;
import org.server.remoteclass.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService){
        this.orderService = orderService;
    }

    //주문 신청
    @PostMapping
    public ResponseEntity<OrderDto> createOrder(@RequestBody @Valid OrderFormDto orderFormDto,List<OrderLecture> orderLectures) throws IdNotExistException {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder(orderFormDto, orderLectures));
    }

    //주문 취소
    @PutMapping("/{orderId}")
    public ResponseEntity<OrderDto> createOrder(@PathVariable Long orderId) throws IdNotExistException {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.cancelOrder(orderId));
    }


    //주문 목록 조회
    @GetMapping("/list")
    public ResponseEntity<List<OrderDto>> getMyOrder() throws IdNotExistException {
        return ResponseEntity.ok(orderService.getOrdersByUserId());
    }


}