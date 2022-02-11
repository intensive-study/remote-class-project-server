package org.server.remoteclass.service;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.server.remoteclass.dto.*;
import org.server.remoteclass.entity.Coupon;
import org.server.remoteclass.entity.IssuedCoupon;
import org.server.remoteclass.entity.User;
import org.server.remoteclass.exception.IdNotExistException;
import org.server.remoteclass.exception.ResultCode;
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
    public IssuedCouponDto issueCoupon(IssuedCouponDto issuedCouponDto) throws IdNotExistException {
        User user = SecurityUtil.getCurrentUserEmail()
                .flatMap(userRepository::findByEmail)
                .orElseThrow(() -> new IdNotExistException("존재하지 않는 사용자", ResultCode.ID_NOT_EXIST));

        Coupon coupon = couponRepository.findByCouponCode(issuedCouponDto.getCouponCode())
                .orElseThrow(() -> new IdNotExistException("존재하지 않는 쿠폰 번호입니다.", ResultCode.ID_NOT_EXIST));

//        IssuedCoupon check = couponRepository.findByCouponCode(issuedCouponDto.getCouponCode());
        //발급 쿠폰 중복 조회 필요
       IssuedCoupon issuedCoupon = modelMapper.map(issuedCouponDto, IssuedCoupon.class);
        issuedCoupon.setCouponUsed(false);
        issuedCoupon.setCouponValidDate(LocalDateTime.now().plusDays(coupon.getCouponValidDays()));
        issuedCoupon.setUser(user);
        issuedCoupon.setCoupon(coupon);
        log.info("issued Coupon으로 couponId 확인 : " + issuedCoupon.getCoupon().getCouponId());
        return IssuedCouponDto.from(issuedCouponRepository.save(issuedCoupon));
    }

    @Override
    public List<IssuedCouponDto> getAllMyCoupons() throws IdNotExistException {
        User user = SecurityUtil.getCurrentUserEmail()
                .flatMap(userRepository::findByEmail)
                .orElseThrow(() -> new IdNotExistException("존재하지 않는 사용자", ResultCode.ID_NOT_EXIST));

        List<IssuedCoupon> issuedCouponList = issuedCouponRepository.findByUser(user.getUserId());
        return issuedCouponList.stream()
                .map(issuedCoupon ->
                        IssuedCouponDto.from(issuedCoupon)
//                        modelMapper.map(issuedCoupon, IssuedCouponDto.class)
                ).collect(Collectors.toList());
    }

    @Override
    public IssuedCouponDto getMyCoupon(Long issuedCouponId) throws IdNotExistException {
        User user = SecurityUtil.getCurrentUserEmail()
                .flatMap(userRepository::findByEmail)
                .orElseThrow(() -> new IdNotExistException("존재하지 않는 사용자", ResultCode.ID_NOT_EXIST));

        return IssuedCouponDto
                .from(issuedCouponRepository.findByUserAndIssuedCouponId(user.getUserId(), issuedCouponId).orElse(null));
    }
}
