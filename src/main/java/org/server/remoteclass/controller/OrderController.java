package org.server.remoteclass.controller;

import org.server.remoteclass.dto.LectureDto;
import org.server.remoteclass.dto.OrderDto;
import org.server.remoteclass.exception.IdNotExistException;
import org.server.remoteclass.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {

    OrderService orderService;


    //주문 목록 조회
    @GetMapping("/list")
    public ResponseEntity<List<OrderDto>> getMyOrder() throws IdNotExistException {
        return ResponseEntity.ok(orderService.getOrdersByUserId());
    }


}
