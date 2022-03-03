package org.server.remoteclass.service.cart;

import org.server.remoteclass.dto.cart.RequestCartDto;
import org.server.remoteclass.dto.cart.ResponseCartListDto;


public interface CartService {
    void createCart(RequestCartDto requestCartDto);
    void deleteCart(Long lectureId);
    void deleteAllCart();
    ResponseCartListDto getCartsByUserId();
    Boolean checkIfLectureInCart(Long lectureId, Long userId);
    Boolean checkIfUserInCart(Long userId);
}