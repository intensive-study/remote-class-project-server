package org.server.remoteclass.service.purchase;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.server.remoteclass.constant.OrderStatus;
import org.server.remoteclass.dto.purchase.PurchaseDto;
import org.server.remoteclass.dto.purchase.RequestPurchaseDto;
import org.server.remoteclass.dto.purchase.ResponsePurchaseDto;
import org.server.remoteclass.entity.*;
import org.server.remoteclass.exception.ForbiddenException;
import org.server.remoteclass.exception.IdNotExistException;
import org.server.remoteclass.exception.ErrorCode;
import org.server.remoteclass.jpa.*;
import org.server.remoteclass.util.BeanConfiguration;
import org.server.remoteclass.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
public class PurchaseServiceImpl implements PurchaseService{

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final IssuedCouponRepository issuedCouponRepository;
    private final PurchaseRepository purchaseRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public PurchaseServiceImpl(UserRepository userRepository, OrderRepository orderRepository,
                               PurchaseRepository purchaseRepository, IssuedCouponRepository issuedCouponRepository,
                                BeanConfiguration beanConfiguration){
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.issuedCouponRepository = issuedCouponRepository;
        this.purchaseRepository = purchaseRepository;
        this.modelMapper = beanConfiguration.modelMapper();
    }

    @Override
    @Transactional
    public PurchaseDto createPurchase(RequestPurchaseDto requestPurchaseDto) {
        User user = SecurityUtil.getCurrentUserEmail()
                .flatMap(userRepository::findByEmail)
                .orElseThrow(() -> new IdNotExistException("현재 로그인 상태가 아닙니다.", ErrorCode.ID_NOT_EXIST));
        //orderId를 입력하면 order 객체 받아오기
        Order order = orderRepository.findById(requestPurchaseDto.getOrderId())
                .orElseThrow(() -> new IdNotExistException("유효한 주문 번호가 아닙니다.", ErrorCode.ID_NOT_EXIST));
        Purchase purchase = null;
        // pending상태만 주문 가능. 취소되거나 이미 완료된 주문이면 더이상 구매하지 못함.
        if(order.getOrderStatus() == OrderStatus.PENDING){
            purchase = modelMapper.map(requestPurchaseDto, Purchase.class);
            purchase.setPurchaseDate(LocalDateTime.now());
            order.setOrderStatus(OrderStatus.COMPLETE);//해당 orderId의 주문에는 status를 complete로 변경하기
            purchase.setOrder(order);
            purchase.setPurchasePrice(order.getOriginalPrice());
            purchaseRepository.save(purchase);
        }
        else{
            throw new ForbiddenException("주문 가능한 상태가 아닙니다", ErrorCode.FORBIDDEN);
        }
        /** 쿠폰 할인 관련 기능!!
        // 주문에서 정상가 끌어오고
        Integer totalPrice = order.getOriginalPrice();
        //쿠폰이 있으면
        if(order.getIssuedCoupon()){
            //쿠폰 객체 끌어오고
            IssuedCoupon issuedCoupon = issuedCouponRepository.findByUserAndIssuedCouponId(user.getUserId(), order.getIssuedCoupon().getIssuedCouponId());
            // how to map issued-coupon-fix/rate??????
            //쿠폰이 정률인지 정액인지
            if(fix){ //정액쿠폰
                totalPrice -= discountPrice;
            }
            if(rate){
                totalPrice *= (1-discountRate);
            }
        }
        purchase.setPurchasePrice(totalPrice);
         */
        return PurchaseDto.from(purchase);
    }

    //전체 구매내역 조회
    @Override
    public List<ResponsePurchaseDto> getAllPurchaseByUserId(){
        User user = SecurityUtil.getCurrentUserEmail()
                .flatMap(userRepository::findByEmail)
                .orElseThrow(() -> new IdNotExistException("현재 로그인 상태가 아닙니다.", ErrorCode.ID_NOT_EXIST));
        List<Purchase> purchases = purchaseRepository.findByOrder_User_UserIdOrderByPurchaseDateDesc(user.getUserId());

        return purchases.stream().map(ResponsePurchaseDto::new).collect(Collectors.toList());
    }

    // 특정 구매내역 조회
    @Override
    public ResponsePurchaseDto getPurchaseByUserIdAndPurchaseId(Long purchaseId) throws IdNotExistException{
        SecurityUtil.getCurrentUserEmail()
                .flatMap(userRepository::findByEmail)
                .orElseThrow(() -> new IdNotExistException("현재 로그인 상태가 아닙니다.", ErrorCode.ID_NOT_EXIST));

        // 조회를 예외처리 해 줄 필요가 있나요? 오히려 올바른 범위가 아닌 경우에만(-1과 같은 경우)에만 해주면 될 것 같은데요..
        // 그냥 null을 반환해주면 되지 않을까요?
        Purchase purchase = purchaseRepository.findById(purchaseId)
                .orElseThrow(() -> new IdNotExistException("존재하지 않는 주문", ErrorCode.ID_NOT_EXIST));

        return new ResponsePurchaseDto(purchase);
    }
}