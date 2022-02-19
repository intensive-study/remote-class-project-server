package org.server.remoteclass.service.fixDiscountCoupon;

import org.modelmapper.ModelMapper;
import org.server.remoteclass.dto.fixDiscountCoupon.RequestFixDiscountCouponDto;
import org.server.remoteclass.dto.fixDiscountCoupon.ResponseFixDiscountCouponDto;
import org.server.remoteclass.entity.FixDiscountCoupon;
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
    private final FixDiscountCouponRepository fixDiscountCouponRepository;

    public FixDiscountCouponServiceImpl(BeanConfiguration beanConfiguration,
                                        FixDiscountCouponRepository fixDiscountCouponRepository){
        this.modelMapper = beanConfiguration.modelMapper();
        this.fixDiscountCouponRepository = fixDiscountCouponRepository;
    }

    @Override
    public void createFixDiscountCoupon(RequestFixDiscountCouponDto requestFixDiscountCouponDto) {
        FixDiscountCoupon fixDiscountCoupon = modelMapper.map(requestFixDiscountCouponDto, FixDiscountCoupon.class);
        fixDiscountCoupon.setCouponCode(UUID.randomUUID().toString());
        fixDiscountCoupon.setCouponValid(true);
        fixDiscountCoupon.setCouponValidDays(requestFixDiscountCouponDto.getCouponValidDays());
        fixDiscountCoupon.setDiscountPrice(requestFixDiscountCouponDto.getDiscountPrice());
        fixDiscountCouponRepository.save(fixDiscountCoupon);
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
}
