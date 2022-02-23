package org.server.remoteclass.service.cart;

import org.server.remoteclass.dto.cart.RequestCartDto;
import org.server.remoteclass.dto.cart.ResponseCartDto;

import java.util.List;

public interface CartService {

    // 장바구니 추가
    void createCart(RequestCartDto requestCartDto);
    // 장바구니 삭제
    void deleteCart(Long lectureId);
    // 장바구니 전체 비우기
    void deleteAllCart();

    //나의 장바구니 리스트
    List<ResponseCartDto> getCartsByUserId();
    // 장바구니 금액 계산
    Integer sumCartByUserId();
    // 장바구니 개수 계산
    Integer countCartByUserId();

}