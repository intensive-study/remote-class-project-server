package org.server.remoteclass.service.fixDiscountCoupon;

import org.modelmapper.ModelMapper;
import org.server.remoteclass.dto.coupon.ResponseCouponDto;
import org.server.remoteclass.dto.fixDiscountCoupon.RequestFixDiscountCouponDto;
import org.server.remoteclass.dto.fixDiscountCoupon.RequestUpdateFixDiscountCouponDto;
import org.server.remoteclass.dto.fixDiscountCoupon.ResponseFixDiscountCouponDto;
import org.server.remoteclass.entity.FixDiscountCoupon;
import org.server.remoteclass.exception.ErrorCode;
import org.server.remoteclass.exception.IdNotExistException;
import org.server.remoteclass.jpa.CouponRepository;
import org.server.remoteclass.jpa.FixDiscountCouponRepository;
import org.server.remoteclass.util.BeanConfiguration;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class FixDiscountCouponServiceImpl implements FixDiscountCouponService {

    private final ModelMapper modelMapper;
    private final CouponRepository couponRepository;
    private final FixDiscountCouponRepository fixDiscountCouponRepository;

    public FixDiscountCouponServiceImpl(BeanConfiguration beanConfiguration,
                                        FixDiscountCouponRepository fixDiscountCouponRepository,
                                        CouponRepository couponRepository){
        this.modelMapper = beanConfiguration.modelMapper();
        this.fixDiscountCouponRepository = fixDiscountCouponRepository;
        this.couponRepository = couponRepository;
    }

    @Override
    public ResponseFixDiscountCouponDto createFixDiscountCoupon(RequestFixDiscountCouponDto requestFixDiscountCouponDto) {
        FixDiscountCoupon fixDiscountCoupon = modelMapper.map(requestFixDiscountCouponDto, FixDiscountCoupon.class);
        fixDiscountCoupon.setCouponCode(UUID.randomUUID().toString());
        fixDiscountCoupon.setCouponValid(true);
        fixDiscountCoupon.setCouponValidDays(requestFixDiscountCouponDto.getCouponValidDays());
        fixDiscountCoupon.setDiscountPrice(requestFixDiscountCouponDto.getDiscountPrice());
        return ResponseFixDiscountCouponDto.from(fixDiscountCouponRepository.save(fixDiscountCoupon));
//        couponRepository.save(fixDiscountCoupon); -> couponRepository로 저장해도 됨.
    }

    @Override
    public List<ResponseFixDiscountCouponDto> getAllFixDiscountCoupons() {
        List<FixDiscountCoupon> fixDiscountCouponList = fixDiscountCouponRepository.findAll();
        return fixDiscountCouponList.stream()
                .map(fixDiscount ->
                        ResponseFixDiscountCouponDto.from(fixDiscount)
                ).collect(Collectors.toList());
    }

    @Override
    public ResponseFixDiscountCouponDto getFixDiscountCoupon(Long couponId) {
        return ResponseFixDiscountCouponDto.from(fixDiscountCouponRepository.findByCouponId(couponId).orElse(null));
    }

    @Override
    public void updateFixDiscountCoupon(RequestUpdateFixDiscountCouponDto requestUpdateFixDiscountCouponDto) {
        FixDiscountCoupon fixDiscountCoupon = fixDiscountCouponRepository.findByCouponId(requestUpdateFixDiscountCouponDto.getCouponId())
                .orElseThrow(() -> new IdNotExistException("해당 강의가 존재하지 않습니다.", ErrorCode.ID_NOT_EXIST));

        fixDiscountCoupon.setDiscountPrice(requestUpdateFixDiscountCouponDto.getDiscountPrice());
        fixDiscountCoupon.setCouponValidDays(requestUpdateFixDiscountCouponDto.getCouponValidDays());
        fixDiscountCoupon.setTitle(requestUpdateFixDiscountCouponDto.getTitle());
        // 쿠폰코드는 수정할 수 없도록 하였습니다. 쿠폰코드가 변경될 경우 더 큰 혼란이 올 것이라 생각하였습니다.
        // 쿠폰에 문제가 생긴 경우, 바로 비활성화를 하는 게 맞다고 생각했습니다.
        fixDiscountCouponRepository.save(fixDiscountCoupon);
    }
}
