package org.server.remoteclass.service.cart;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.server.remoteclass.dto.cart.RequestCartDto;
import org.server.remoteclass.dto.cart.ResponseCartDto;
import org.server.remoteclass.dto.cart.ResponseCartListDto;
import org.server.remoteclass.entity.Cart;
import org.server.remoteclass.entity.User;
import org.server.remoteclass.exception.BadRequestArgumentException;
import org.server.remoteclass.exception.ErrorCode;
import org.server.remoteclass.exception.IdNotExistException;
import org.server.remoteclass.jpa.CartRepository;
import org.server.remoteclass.jpa.LectureRepository;
import org.server.remoteclass.jpa.StudentRepository;
import org.server.remoteclass.jpa.UserRepository;
import org.server.remoteclass.util.AccessVerification;
import org.server.remoteclass.util.BeanConfiguration;
import org.server.remoteclass.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class CartServiceImpl implements CartService {

    private final UserRepository userRepository;
    private final LectureRepository lectureRepository;
    private final StudentRepository studentRepository;
    private final CartRepository cartRepository;
    private final ModelMapper modelMapper;
    private final AccessVerification accessVerification;

    @Autowired
    public CartServiceImpl(UserRepository userRepository, LectureRepository lectureRepository,
                           StudentRepository studentRepository, CartRepository cartRepository,
                            BeanConfiguration beanConfiguration,AccessVerification accessVerification){
        this.userRepository = userRepository;
        this.lectureRepository = lectureRepository;
        this.studentRepository = studentRepository;
        this.cartRepository = cartRepository;
        this.modelMapper = beanConfiguration.modelMapper();
        this.accessVerification = accessVerification;
    }

    @Override
    public void createCart(RequestCartDto requestCartDto) {
        User user = SecurityUtil.getCurrentUserEmail()
                .flatMap(userRepository::findByEmail)
                .orElseThrow(() -> new IdNotExistException("존재하지 않는 사용자", ErrorCode.ID_NOT_EXIST));

        //이미 수강한 수업이 아니면서 장바구니에 없는 강의일때 장바구니에 넣을 수 있습니다.
        Cart cart = new Cart();
        if(studentRepository.existsByLecture_LectureIdAndUser_UserId(requestCartDto.getLectureId(), user.getUserId())) {
            throw new BadRequestArgumentException("이미 수강한 강의입니다.", ErrorCode.BAD_REQUEST_ARGUMENT);
        }
        else{
            if(cartRepository.existsByLecture_LectureIdAndUser_UserId(requestCartDto.getLectureId(), user.getUserId())) {
                throw new BadRequestArgumentException("장바구니에 있는 강의입니다", ErrorCode.BAD_REQUEST_ARGUMENT);
            }
            else {
                cart.setUser(user);
                cart.setLecture(lectureRepository.findById(requestCartDto.getLectureId()).orElse(null));
                cartRepository.save(cart);
            }
        }
    }

    @Override
    public void deleteCart(Long lectureId) {
        User user = SecurityUtil.getCurrentUserEmail()
                .flatMap(userRepository::findByEmail)
                .orElseThrow(() -> new IdNotExistException("존재하지 않는 사용자", ErrorCode.ID_NOT_EXIST));
        if(cartRepository.existsByLecture_LectureIdAndUser_UserId(lectureId, user.getUserId())){
            cartRepository.deleteByLecture_LectureIdAndUser_UserId(lectureId, user.getUserId());
        }
        else{
            throw new IdNotExistException("장바구니에 없는 강의입니다.", ErrorCode.ID_NOT_EXIST);
        }
    }

    @Override
    public void deleteAllCart() {
        User user = SecurityUtil.getCurrentUserEmail()
                .flatMap(userRepository::findByEmail)
                .orElseThrow(() -> new IdNotExistException("존재하지 않는 사용자", ErrorCode.ID_NOT_EXIST));
        // 카트에 하나라도 상품 존재하면
        if(cartRepository.existsByUser_UserId(user.getUserId())){
            cartRepository.deleteAllByUser_UserId(user.getUserId());
        }
        else{
            throw new IdNotExistException("장바구니가 비어있습니다.", ErrorCode.ID_NOT_EXIST);
        }
    }

    //현재 장바구니 전체 리스트 조회
    @Override
    @Transactional(readOnly = true)
    public ResponseCartListDto getCartsByUserId() {

        User user = SecurityUtil.getCurrentUserEmail()
                .flatMap(userRepository::findByEmail)
                .orElseThrow(() -> new IdNotExistException("존재하지 않는 사용자", ErrorCode.ID_NOT_EXIST));

        ResponseCartListDto responseCartListDto = new ResponseCartListDto();
        List<Cart> carts = cartRepository.findByUser_UserIdOrderByCreatedDateDesc(user.getUserId());
        responseCartListDto.setResponseCartDtoList(carts.stream()
                .map(cart->ResponseCartDto.from(cart)).collect(Collectors.toList()));
        responseCartListDto.setSumCart(cartRepository.findSumCartByUserId(user.getUserId()));
        responseCartListDto.setCountCart(cartRepository.findCountCartByUserId(user.getUserId()));

        return responseCartListDto;
    }

}