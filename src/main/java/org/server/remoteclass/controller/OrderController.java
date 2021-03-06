package org.server.remoteclass.controller;


import io.swagger.annotations.ApiOperation;
import org.server.remoteclass.dto.order.RequestOrderDto;
import org.server.remoteclass.dto.order.ResponseOrderDto;
import org.server.remoteclass.service.order.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@PreAuthorize("hasRole('ROLE_USER')")
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService){
        this.orderService = orderService;
    }


    @ApiOperation("주문 신청")
    @PostMapping
    public ResponseEntity createOrder(@Valid @RequestBody RequestOrderDto requestOrderDto) {
        orderService.createOrder(requestOrderDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @ApiOperation("장바구니 주문 신청")
    @PostMapping("/carts")
    public ResponseEntity createOrderFromCart(@Valid @RequestBody RequestOrderDto requestOrderDto) {
        orderService.createOrderFromCart(requestOrderDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @ApiOperation("사용자 본인 주문 목록 조회")
    @GetMapping
    public ResponseEntity<List<ResponseOrderDto>> getMyOrder() {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.getMyOrdersByUserId());
    }

    @ApiOperation("주문 취소")
    @PutMapping("/cancel/{orderId}")
    public ResponseEntity cancelOrder(@PathVariable("orderId") @Valid Long orderId) {
        orderService.cancelOrder(orderId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @ApiOperation(value = "주문 완료 요청", notes = "구매하기 위해 해당 주문에 complete 요청")
    @PutMapping("/complete/{orderId}")
    public ResponseEntity completeOrder(@PathVariable("orderId") @Valid Long orderId) {
        orderService.completeOrder(orderId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
