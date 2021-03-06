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
            throw new IdNotExistException("???????????? ?????? ???????????????.", ErrorCode.ID_NOT_EXIST);
        }

        if(coupon.getCouponValid() == true){
            throw new StatusDuplicateException("?????? ?????? ???????????????.", ErrorCode.STATUS_DUPLICATION);
        }
        coupon.setCouponValid(true);
    }

    @Override
    @Transactional
    public void deactivateCoupon(Long couponId) {
        //?????????(?)?????? ???????????? orElseThrow ????????? ???????????? ?????? ??? ????????????. ????????? orElse ????????????
        //null ????????? ?????? ????????? ???????????????.

        Coupon coupon = (Coupon) couponRepository.findByCouponId(couponId).orElse(null);
        if(coupon == null){
            throw new IdNotExistException("???????????? ?????? ???????????????.", ErrorCode.ID_NOT_EXIST);
        }

        if(coupon.getCouponValid() == false){
            throw new StatusDuplicateException("?????? ?????? ???????????????.", ErrorCode.STATUS_DUPLICATION);
        }
        coupon.setCouponValid(false);
    }

    @Override
    public ResponseCouponDto getCouponByCouponCode(String couponCode) {
        return ResponseCouponDto.from((Coupon) couponRepository.findByCouponCode(couponCode).orElse(null));
    }

    //?????? ??????
    @Override
    @Transactional
    public void deleteCoupon(Long couponId) {
        //?????????(?)?????? ???????????? orElseThrow ????????? ???????????? ?????? ??? ????????????. ????????? orElse ??????????????????.
        Coupon coupon = (Coupon) couponRepository.findByCouponId(couponId).orElse(null);
        if(coupon == null){
            throw new IdNotExistException("???????????? ?????? ???????????????.", ErrorCode.ID_NOT_EXIST);
        }
        couponRepository.deleteByCouponId(couponId);
    }

}
