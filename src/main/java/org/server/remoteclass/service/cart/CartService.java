package org.server.remoteclass.service.cart;

import org.server.remoteclass.dto.cart.RequestCartDto;
import org.server.remoteclass.dto.cart.ResponseCartListDto;


public interface CartService {

    // 장바구니 추가
    void createCart(RequestCartDto requestCartDto);
    // 장바구니 삭제
    void deleteCart(Long lectureId);
    // 장바구니 전체 비우기
    void deleteAllCart();

    //나의 장바구니 리스트
    ResponseCartListDto getCartsByUserId();

}