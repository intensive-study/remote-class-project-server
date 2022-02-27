package org.server.remoteclass.service.coupon;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.server.remoteclass.dto.coupon.RequestCouponDto;
import org.server.remoteclass.dto.coupon.ResponseCouponDto;
import org.server.remoteclass.entity.Coupon;
import org.server.remoteclass.entity.FixDiscountCoupon;
import org.server.remoteclass.entity.RateDiscountCoupon;
import org.server.remoteclass.exception.ErrorCode;
import org.server.remoteclass.exception.IdNotExistException;
import org.server.remoteclass.exception.StatusDuplicateException;
import org.server.remoteclass.jpa.CouponRepository;
import org.server.remoteclass.util.BeanConfiguration;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional(readOnly = true)
public class CouponServiceImpl implements CouponService {

    private final CouponRepository couponRepository;
    private final ModelMapper modelMapper;

    public CouponServiceImpl(CouponRepository couponRepository, BeanConfiguration beanConfiguration) {
        this.couponRepository = couponRepository;
        this.modelMapper = beanConfiguration.modelMapper();
    }

    @Override
    public ResponseCouponDto getCouponByCouponId(Long couponId) {

        Coupon coupon = (Coupon) couponRepository.findByCouponId(couponId).orElse(null);
        if (coupon instanceof FixDiscountCoupon) {
            return ResponseCouponDto.from((FixDiscountCoupon) coupon);
        }
        return ResponseCouponDto.from((RateDiscountCoupon) coupon);

    }

    @Override
    public List<ResponseCouponDto> getAllCoupons() {
        List<Coupon> coupons = couponRepository.findAll();
        List<ResponseCouponDto> list = new ArrayList<>();

        for (Coupon coupon : coupons) {
            if (coupon instanceof FixDiscountCoupon) {
                list.add(ResponseCouponDto.from((FixDiscountCoupon) coupon));
            }

            else if(coupon instanceof RateDiscountCoupon){
                list.add(ResponseCouponDto.from((RateDiscountCoupon) coupon));
            }
        }
        return list;
    }

    @Override
    @Transactional
    public void activateCoupon(Long couponId) {
        Coupon coupon = (Coupon) couponRepository.findByCouponId(couponId).orElse(null);

        if(coupon == null){
            throw new IdNotExistException("존재하지 않는 쿠폰입니다.", ErrorCode.ID_NOT_EXIST);
        }

        if(coupon.getCouponValid() == true){
            throw new StatusDuplicateException("이미 해당 상태입니다.", ErrorCode.STATUS_DUPLICATION);
        }
        coupon.setCouponValid(true);
    }

    @Override
    @Transactional
    public void deactivateCoupon(Long couponId) {
        //제너릭(?)으로 구현해서 orElseThrow 문법이 작동하지 않는 것 같습니다. 그래서 orElse 사용하여
        //null 여부로 에러 처리를 해줬습니다.

        Coupon coupon = (Coupon) couponRepository.findByCouponId(couponId).orElse(null);
        if(coupon == null){
            throw new IdNotExistException("존재하지 않는 쿠폰입니다.", ErrorCode.ID_NOT_EXIST);
        }

        if(coupon.getCouponValid() == false){
            throw new StatusDuplicateException("이미 해당 상태입니다.", ErrorCode.STATUS_DUPLICATION);
        }
        coupon.setCouponValid(false);
    }

    @Override
    @Transactional
    public ResponseCouponDto createCoupon(RequestCouponDto requestCouponDto) {
        Coupon coupon = modelMapper.map(requestCouponDto, Coupon.class);
        coupon.setCouponCode(UUID.randomUUID().toString());
        coupon.setCouponValid(true);
        coupon.setCouponValidDays(requestCouponDto.getCouponValidDays());
        return ResponseCouponDto.from((Coupon) couponRepository.save(coupon));
    }

    @Override
    public ResponseCouponDto getCouponByCouponCode(String couponCode) {
        return ResponseCouponDto.from((Coupon) couponRepository.findByCouponCode(couponCode).orElse(null));
    }

    //쿠폰 삭제
    @Override
    @Transactional
    public void deleteCoupon(Long couponId) {
        //제너릭(?)으로 구현해서 orElseThrow 문법이 작동하지 않는 것 같습니다. 그래서 orElse 사용했습니다.
        Coupon coupon = (Coupon) couponRepository.findByCouponId(couponId).orElse(null);
        if(coupon == null){
            throw new IdNotExistException("존재하지 않는 쿠폰입니다.", ErrorCode.ID_NOT_EXIST);
        }
        couponRepository.deleteByCouponId(couponId);
    }

}
