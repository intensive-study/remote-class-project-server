package org.server.remoteclass.service.rateDiscountCoupon;

import org.modelmapper.ModelMapper;
import org.server.remoteclass.dto.rateDiscountCoupon.RequestRateDiscountCouponDto;
import org.server.remoteclass.dto.rateDiscountCoupon.RequestUpdateRateDiscountCouponDto;
import org.server.remoteclass.dto.rateDiscountCoupon.ResponseRateDiscountCouponDto;
import org.server.remoteclass.entity.RateDiscountCoupon;
import org.server.remoteclass.exception.ErrorCode;
import org.server.remoteclass.exception.IdNotExistException;
import org.server.remoteclass.jpa.RateDiscountCouponRepository;
import org.server.remoteclass.util.BeanConfiguration;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class RateDiscountCouponServiceImpl implements RateDiscountCouponService {

    private final ModelMapper modelMapper;
    private final RateDiscountCouponRepository rateDiscountCouponRepository;

    public RateDiscountCouponServiceImpl(BeanConfiguration beanConfiguration,
                                         RateDiscountCouponRepository rateDiscountCouponRepository){
        this.modelMapper = beanConfiguration.modelMapper();
        this.rateDiscountCouponRepository = rateDiscountCouponRepository;
    }

    @Override
    public ResponseRateDiscountCouponDto createRateDiscountCoupon(RequestRateDiscountCouponDto requestRateDiscountCouponDto) {
        RateDiscountCoupon rateDiscountCoupon = modelMapper.map(requestRateDiscountCouponDto, RateDiscountCoupon.class);
        rateDiscountCoupon.setCouponCode(UUID.randomUUID().toString());
        rateDiscountCoupon.setCouponValid(true);
        rateDiscountCoupon.setCouponValidDays(requestRateDiscountCouponDto.getCouponValidDays());
        rateDiscountCoupon.setDiscountRate(rateDiscountCoupon.getDiscountRate());
        return ResponseRateDiscountCouponDto.from(rateDiscountCouponRepository.save(rateDiscountCoupon));
    }

    @Override
    public List<ResponseRateDiscountCouponDto> getAllRateDiscountCoupons() {
        List<RateDiscountCoupon> rateDiscountCouponList = rateDiscountCouponRepository.findAll();
        return rateDiscountCouponList.stream().
                map(rateDiscount -> ResponseRateDiscountCouponDto.from(rateDiscount)).collect(Collectors.toList());
    }

    @Override
    public ResponseRateDiscountCouponDto getRateDiscountCoupon(Long couponId) {
        return ResponseRateDiscountCouponDto.from(rateDiscountCouponRepository.findByCouponId(couponId).orElse(null));
    }

    @Override
    public void updateRateDiscountCoupon(RequestUpdateRateDiscountCouponDto requestUpdateRateDiscountCouponDto) {
        RateDiscountCoupon rateDiscountCoupon = rateDiscountCouponRepository.findByCouponId(requestUpdateRateDiscountCouponDto.getCouponId())
                .orElseThrow(() -> new IdNotExistException("?????? ????????? ???????????? ????????????", ErrorCode.ID_NOT_EXIST));

        rateDiscountCoupon.setDiscountRate(requestUpdateRateDiscountCouponDto.getDiscountRate());
        rateDiscountCoupon.setCouponValidDays(requestUpdateRateDiscountCouponDto.getCouponValidDays());
        rateDiscountCoupon.setTitle(requestUpdateRateDiscountCouponDto.getTitle());
        // ??????????????? ???????????? ????????? ???????????????. ?????? ????????? ????????? ?????? ??????????????? ?????? ??? ????????? ??????????????????.

        rateDiscountCouponRepository.save(rateDiscountCoupon);
    }
}
