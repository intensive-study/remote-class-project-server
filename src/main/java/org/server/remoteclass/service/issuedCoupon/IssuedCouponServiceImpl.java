package org.server.remoteclass.service.issuedCoupon;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.server.remoteclass.dto.issuedcoupon.RequestIssuedCouponDto;
import org.server.remoteclass.dto.issuedcoupon.ResponseIssuedCouponDto;
import org.server.remoteclass.entity.Coupon;
import org.server.remoteclass.entity.IssuedCoupon;
import org.server.remoteclass.entity.User;
import org.server.remoteclass.exception.CouponDuplicateException;
import org.server.remoteclass.exception.IdNotExistException;
import org.server.remoteclass.exception.ErrorCode;
import org.server.remoteclass.jpa.CouponRepository;
import org.server.remoteclass.jpa.IssuedCouponRepository;
import org.server.remoteclass.jpa.UserRepository;
import org.server.remoteclass.util.BeanConfiguration;
import org.server.remoteclass.util.SecurityUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
public class IssuedCouponServiceImpl implements IssuedCouponService{

    private final IssuedCouponRepository issuedCouponRepository;
    private final CouponRepository couponRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public IssuedCouponServiceImpl(IssuedCouponRepository issuedCouponRepository,
                                   BeanConfiguration beanConfiguration,
                                   UserRepository userRepository,
                                   CouponRepository couponRepository) {
        this.issuedCouponRepository = issuedCouponRepository;
        this.modelMapper = beanConfiguration.modelMapper();
        this.userRepository = userRepository;
        this.couponRepository = couponRepository;
    }

    // 테스트 필요
    @Override
    @Transactional
    public void issueCoupon(RequestIssuedCouponDto requestIssuedCouponDto){
        User user = SecurityUtil.getCurrentUserEmail()
                .flatMap(userRepository::findByEmail)
                .orElseThrow(() -> new IdNotExistException("존재하지 않는 사용자입니다.", ErrorCode.ID_NOT_EXIST));

        Coupon coupon = (Coupon) couponRepository.findByCouponCode(requestIssuedCouponDto.getCouponCode()).orElse(null);
        if(coupon == null){
            throw new IdNotExistException("존재하지 않는 쿠폰 번호입니다", ErrorCode.ID_NOT_EXIST);
        }

        if(issuedCouponRepository.existsByUserAndCoupon(user.getUserId(), coupon.getCouponId()).isPresent()){
            throw new CouponDuplicateException("이미 발급받은 쿠폰입니다.", ErrorCode.COUPON_DUPLICATION);
        }

        IssuedCoupon issuedCoupon = new IssuedCoupon();
        issuedCoupon.setCoupon(coupon);
        issuedCoupon.setCouponUsed(false);
        issuedCoupon.setCouponValidDate(LocalDateTime.now().plusDays(coupon.getCouponValidDays()));
        issuedCoupon.setUser(user);
        log.info("issuedCoupon couponCode" + issuedCoupon.getCoupon().getCouponCode());
        log.info("issued Coupon으로 couponId 확인 : " + issuedCoupon.getCoupon().getCouponId());
        issuedCouponRepository.save(issuedCoupon);
    }

    @Override
    public List<ResponseIssuedCouponDto> getAllMyCoupons() {
        User user = SecurityUtil.getCurrentUserEmail()
                .flatMap(userRepository::findByEmail)
                .orElseThrow(() -> new IdNotExistException("존재하지 않는 사용자", ErrorCode.ID_NOT_EXIST));

        List<IssuedCoupon> issuedCouponList = issuedCouponRepository.findByUser(user.getUserId());
        return issuedCouponList.stream()
                .map(ResponseIssuedCouponDto::from
                ).collect(Collectors.toList());
    }

    @Override
    public ResponseIssuedCouponDto getMyCoupon(Long issuedCouponId) {
        User user = SecurityUtil.getCurrentUserEmail()
                .flatMap(userRepository::findByEmail)
                .orElseThrow(() -> new IdNotExistException("존재하지 않는 사용자", ErrorCode.ID_NOT_EXIST));

        return ResponseIssuedCouponDto
                .from(issuedCouponRepository.findByUserAndIssuedCouponId(user.getUserId(), issuedCouponId).orElse(null));
    }
}
