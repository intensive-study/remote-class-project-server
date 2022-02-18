package org.server.remoteclass.service.cart;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.server.remoteclass.constant.UserRole;
import org.server.remoteclass.dto.cart.CartDto;
import org.server.remoteclass.dto.cart.RequestCartDto;
import org.server.remoteclass.dto.cart.ResponseCartDto;
import org.server.remoteclass.dto.lecture.ResponseLectureDto;
import org.server.remoteclass.dto.student.ResponseStudentByLecturerDto;
import org.server.remoteclass.entity.Cart;
import org.server.remoteclass.entity.Lecture;
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
import java.util.Objects;
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
    public CartDto createCart(RequestCartDto requestCartDto) throws IdNotExistException, NameDuplicateException {
        User user = SecurityUtil.getCurrentUserEmail()
                .flatMap(userRepository::findByEmail)
                .orElseThrow(() -> new IdNotExistException("존재하지 않는 사용자", ResultCode.ID_NOT_EXIST));
        Cart cart = new Cart();
        if(user.getUserRole() == UserRole.ROLE_STUDENT){
            if(!studentRepository.existsByLecture_LectureIdAndUser_UserId(requestCartDto.getLectureId(), user.getUserId())) {
                cart.setUser(user);
                cart.setLecture(lectureRepository.findById(requestCartDto.getLectureId()).orElse(null));
            }
            else{
                throw new NameDuplicateException("이미 수강한 강의입니다.", ResultCode.NAME_DUPLICATION);
            }
        }
        return CartDto.from(cartRepository.save(cart));
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

}