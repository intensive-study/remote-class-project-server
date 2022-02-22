package org.server.remoteclass.service.cart;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.server.remoteclass.constant.UserRole;
import org.server.remoteclass.dto.cart.RequestCartDto;
import org.server.remoteclass.dto.cart.ResponseCartDto;
import org.server.remoteclass.entity.Cart;
import org.server.remoteclass.entity.User;
import org.server.remoteclass.exception.ForbiddenException;
import org.server.remoteclass.exception.IdNotExistException;
import org.server.remoteclass.exception.NameDuplicateException;
import org.server.remoteclass.exception.ResultCode;
import org.server.remoteclass.jpa.CartRepository;
import org.server.remoteclass.jpa.LectureRepository;
import org.server.remoteclass.jpa.StudentRepository;
import org.server.remoteclass.jpa.UserRepository;
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

    @Autowired
    public CartServiceImpl(UserRepository userRepository, LectureRepository lectureRepository,
                           StudentRepository studentRepository, CartRepository cartRepository,
                            BeanConfiguration beanConfiguration){
        this.userRepository = userRepository;
        this.lectureRepository = lectureRepository;
        this.studentRepository = studentRepository;
        this.cartRepository = cartRepository;
        this.modelMapper = beanConfiguration.modelMapper();
    }

    @Override
    public ResponseCartDto createCart(RequestCartDto requestCartDto) throws IdNotExistException, NameDuplicateException {
        User user = SecurityUtil.getCurrentUserEmail()
                .flatMap(userRepository::findByEmail)
                .orElseThrow(() -> new IdNotExistException("존재하지 않는 사용자", ResultCode.ID_NOT_EXIST));
        Cart cart = new Cart();
        if(user.getUserRole() == UserRole.ROLE_STUDENT){
            //이미 수강한 수업이 아니면서 장바구니에 없는 강의일때 장바구니에 넣을 수 있습니다.
            if(!studentRepository.existsByLecture_LectureIdAndUser_UserId(requestCartDto.getLectureId(), user.getUserId())) {
                if(!cartRepository.existsByLecture_LectureIdAndUser_UserId(requestCartDto.getLectureId(), user.getUserId())) {
                    cart.setUser(user);
                    cart.setLecture(lectureRepository.findById(requestCartDto.getLectureId()).orElse(null));
                }
                else{
                    throw new NameDuplicateException("장바구니에 있는 강의입니다", ResultCode.NAME_DUPLICATION);
                }
            }
            else{
                throw new NameDuplicateException("이미 수강한 강의입니다.", ResultCode.NAME_DUPLICATION);
            }
        }
        return ResponseCartDto.from(cartRepository.save(cart));
    }

    @Override
    public void deleteCart(Long lectureId) throws IdNotExistException {
        User user = SecurityUtil.getCurrentUserEmail()
                .flatMap(userRepository::findByEmail)
                .orElseThrow(() -> new IdNotExistException("존재하지 않는 사용자", ResultCode.ID_NOT_EXIST));
        if(cartRepository.existsByLecture_LectureIdAndUser_UserId(lectureId, user.getUserId())){
            cartRepository.deleteByLecture_LectureIdAndUser_UserId(lectureId, user.getUserId());
        }
        else{
            throw new IdNotExistException("장바구니에 없는 강의입니다.", ResultCode.ID_NOT_EXIST);
        }
    }

    @Override
    public void deleteAllCart() throws IdNotExistException{
        User user = SecurityUtil.getCurrentUserEmail()
                .flatMap(userRepository::findByEmail)
                .orElseThrow(() -> new IdNotExistException("존재하지 않는 사용자", ResultCode.ID_NOT_EXIST));
        // 카트에 하나라도 상품 존재하면
        if(cartRepository.existsByUser_UserId(user.getUserId())){
            log.info("exists!!!");
            cartRepository.deleteAllByUser_UserId(user.getUserId());
        }
        else{
            throw new IdNotExistException("더이상 강의가 존재하지 않습니다.", ResultCode.ID_NOT_EXIST);
        }

    }

    //현재 장바구니 전체 리스트 조회
    @Override
    @Transactional(readOnly = true)
    public List<ResponseCartDto> getCartsByUserId() throws IdNotExistException, ForbiddenException {

        User user = SecurityUtil.getCurrentUserEmail()
                .flatMap(userRepository::findByEmail)
                .orElseThrow(() -> new IdNotExistException("존재하지 않는 사용자", ResultCode.ID_NOT_EXIST));

        List<Cart> carts = null;
        if(user.getUserRole() == UserRole.ROLE_STUDENT){
            carts = cartRepository.findByUser_UserIdOrderByCreatedDateDesc(user.getUserId());
        }
        else{
            throw new ForbiddenException("접근 권한이 없습니다.", ResultCode.FORBIDDEN);
        }
        return carts.stream().map(cart->ResponseCartDto.from(cart)).collect(Collectors.toList());
    }


    //장바구니 합
    @Override
    public Integer sumCartByUserId() throws IdNotExistException {
        User user = SecurityUtil.getCurrentUserEmail()
                .flatMap(userRepository::findByEmail)
                .orElseThrow(() -> new IdNotExistException("존재하지 않는 사용자", ResultCode.ID_NOT_EXIST));

        return cartRepository.findSumCartByUserId(user.getUserId());
    }

    // 장바구니 개수
    @Override
    public Integer countCartByUserId() throws IdNotExistException {
        User user = SecurityUtil.getCurrentUserEmail()
                .flatMap(userRepository::findByEmail)
                .orElseThrow(() -> new IdNotExistException("존재하지 않는 사용자", ResultCode.ID_NOT_EXIST));

        return cartRepository.findCountCartByUserId(user.getUserId());
    }
}