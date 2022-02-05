package org.server.remoteclass.service;

import org.modelmapper.ModelMapper;
import org.server.remoteclass.dto.CouponDto;
import org.server.remoteclass.entity.Coupon;
import org.server.remoteclass.jpa.CouponRepository;
import org.server.remoteclass.util.BeanConfiguration;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class CouponServiceImpl implements CouponService {

    private final CouponRepository couponRepository;
    private final ModelMapper modelMapper;

    public CouponServiceImpl(CouponRepository couponRepository, BeanConfiguration beanConfiguration){
        this.couponRepository = couponRepository;
        this.modelMapper = beanConfiguration.modelMapper();
    }

    @Override
    public CouponDto getCouponByCouponId(Long couponId) {
        return CouponDto.from(couponRepository.findByCouponId(couponId).orElse(null));
    }

    @Override
    public List<CouponDto> getAllCoupons() {
        List<Coupon> coupons = couponRepository.findAll();
        return coupons.stream().map(coupon -> modelMapper.map(coupon, CouponDto.class)).collect(Collectors.toList());
    }

    @Override
    public CouponDto createCoupon(CouponDto couponDto) {
        Coupon coupon = modelMapper.map(couponDto, Coupon.class);
        coupon.setCouponCode(UUID.randomUUID().toString());
        coupon.setStartDate(couponDto.getStartDate());
        coupon.setCouponValidTime(coupon.getCouponValidTime());
        return CouponDto.from(couponRepository.save(coupon));
    }
}
