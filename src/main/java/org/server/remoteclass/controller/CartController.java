package org.server.remoteclass.controller;

import akka.http.javadsl.Http;
import io.swagger.annotations.ApiOperation;
import org.server.remoteclass.dto.cart.CartDto;
import org.server.remoteclass.dto.cart.RequestCartDto;
import org.server.remoteclass.dto.cart.ResponseCartDto;
import org.server.remoteclass.dto.order.RequestOrderDto;
import org.server.remoteclass.dto.student.RequestStudentDto;
import org.server.remoteclass.dto.student.ResponseStudentByLecturerDto;
import org.server.remoteclass.exception.ForbiddenException;
import org.server.remoteclass.exception.IdNotExistException;
import org.server.remoteclass.exception.NameDuplicateException;
import org.server.remoteclass.service.cart.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/carts")
public class CartController {
    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService){
        this.cartService = cartService;
    }

    @ApiOperation(value = "장바구니 추가", notes = "학생이 신청했던 강의를 장바구니에 넣을 수 있다.")
    @PostMapping
    public ResponseEntity createCart(@RequestBody RequestCartDto requestCartDto) throws IdNotExistException, NameDuplicateException {
        cartService.createCart(requestCartDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @ApiOperation(value = "장바구니 취소", notes = "학생이 신청했던 강의를 장바구니에서 뺄 수 있다.")
    @DeleteMapping("/{lectureId}")
    public ResponseEntity deleteCart(@PathVariable("lectureId") Long lectureId) throws IdNotExistException {
        cartService.deleteCart(lectureId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @ApiOperation(value = "장바구니 전체 삭제", notes = "학생이 신청했던 강의 전체를 장바구니에서 뺄 수 있다.")
    @DeleteMapping("/all")
    public ResponseEntity deleteAllCart() throws IdNotExistException {
        cartService.deleteAllCart();
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}