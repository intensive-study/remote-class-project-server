package org.server.remoteclass.controller;

import io.swagger.annotations.ApiOperation;
import org.server.remoteclass.dto.cart.RequestCartDto;
import org.server.remoteclass.dto.cart.ResponseCartDto;
import org.server.remoteclass.exception.ForbiddenException;
import org.server.remoteclass.exception.IdNotExistException;
import org.server.remoteclass.exception.NameDuplicateException;
import org.server.remoteclass.service.cart.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @ApiOperation(value = "장바구니 개별 삭제", notes = "학생이 신청했던 강의를 장바구니에서 뺄 수 있다.")
    @DeleteMapping("/{lectureId}")
    public ResponseEntity deleteCart(@PathVariable("lectureId") Long lectureId) throws IdNotExistException {
        cartService.deleteCart(lectureId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @ApiOperation(value = "장바구니 전체 삭제", notes = "학생이 신청했던 강의 전체를 장바구니에서 뺄 수 있다.")
    @DeleteMapping("/")
    public ResponseEntity deleteAllCart() throws IdNotExistException {
        cartService.deleteAllCart();
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @ApiOperation(value = "장바구니 조회", notes = "장바구니에 추가한 모든 강의를 조회할 수 있다.")
    @GetMapping
    public ResponseEntity<List<ResponseCartDto>> getAllCartsByUserId() throws IdNotExistException, ForbiddenException {
        return ResponseEntity.status(HttpStatus.OK).body(cartService.getCartsByUserId());
    }

    @ApiOperation(value = "장바구니 가격 합", notes = "장바구니에 추가한 모든 강의의 합을 조회할 수 있다.")
    @GetMapping("/sum")
    public ResponseEntity getSumCartByUserId() throws IdNotExistException {
        return ResponseEntity.status(HttpStatus.OK).body(cartService.sumCartByUserId());
    }

    @ApiOperation(value = "장바구니 개수 합", notes = "장바구니에 추가한 모든 강의 개수를 조회할 수 있다.")
    @GetMapping("/count")
    public ResponseEntity getCountCartByUserId() throws IdNotExistException {
        return ResponseEntity.status(HttpStatus.OK).body(cartService.countCartByUserId());
    }

}