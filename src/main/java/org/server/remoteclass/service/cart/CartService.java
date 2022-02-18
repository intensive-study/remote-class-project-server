package org.server.remoteclass.service.cart;

import org.server.remoteclass.dto.cart.CartDto;
import org.server.remoteclass.dto.cart.RequestCartDto;
import org.server.remoteclass.dto.cart.ResponseCartDto;
import org.server.remoteclass.exception.ForbiddenException;
import org.server.remoteclass.exception.IdNotExistException;
import org.server.remoteclass.exception.NameDuplicateException;

import java.util.List;

public interface CartService {

    // 장바구니 추가
    CartDto createCart(RequestCartDto requestCartDto) throws IdNotExistException, NameDuplicateException;
    // 장바구니 삭제
    void deleteCart(Long lectureId) throws IdNotExistException;
    // 장바구니 전체 비우기
    void deleteAllCart() throws IdNotExistException;

    //나의 장바구니 리스트
    List<ResponseCartDto> getCartsByUserId() throws IdNotExistException, ForbiddenException;

    // 장바구니 금액 계산
    Integer sumCartByUserId() throws IdNotExistException;

    // 장바구니 개수 계산
    Integer countCartByUserId() throws IdNotExistException;

}