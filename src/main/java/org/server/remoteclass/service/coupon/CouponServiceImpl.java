package org.server.remoteclass.service.coupon;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.server.remoteclass.dto.coupon.RequestCouponDto;
import org.server.remoteclass.dto.coupon.ResponseCouponDto;
import org.server.remoteclass.entity.Coupon;
import org.server.remoteclass.entity.FixDiscount;
import org.server.remoteclass.entity.RateDiscount;
import org.server.remoteclass.exception.IdNotExistException;
import org.server.remoteclass.exception.ResultCode;
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
        if (couponRepository.findByCouponId(couponId).orElse(null) instanceof FixDiscount) {
            log.info("FixDiscount 타입입니다.");
            return ResponseCouponDto.from((FixDiscount) couponRepository.findByCouponId(couponId).orElse(null));
        }

        log.info("RateDiscount 타입입니다.");
        return ResponseCouponDto.from((RateDiscount) couponRepository.findByCouponId(couponId).orElse(null));

    }


    @Override
    public List<ResponseCouponDto> getAllCoupons() {
        List<Coupon> coupons = couponRepository.findAll();
        List<ResponseCouponDto> list = new ArrayList<>();

        for (Coupon coupon : coupons) {
            if(coupon instanceof FixDiscount){
                list.add(ResponseCouponDto.from((FixDiscount) coupon));
            }
            else {
                list.add(ResponseCouponDto.from((RateDiscount) coupon));
            }
        }

        return list;
//        return coupons.stream().map(
//                coupon -> ResponseCouponDto.from(coupon)).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deactivateCoupon(Long couponId) throws IdNotExistException {
        Coupon coupon = (Coupon) couponRepository.findByCouponId(couponId).orElse(null);
        coupon.setCouponValid(false);
//        Coupon coupon = (Coupon) couponRepository.findByCouponId(couponId)
//                .orElseThrow(() -> new IdNotExistException("존재하지 않는 쿠폰 번호입니다.", ResultCode.ID_NOT_EXIST)
//        );
//        coupon.setCouponValid(false);
        // 스프링 데이터 JPA가 알아서 해 주는 것 같긴 한데, 일단 save로 업데이트 하였습니다.
//        return CouponDto.from(couponRepository.save(coupon));
    }

    @Override
    @Transactional
    public ResponseCouponDto createCoupon(RequestCouponDto requestCouponDto) {
        // 0 이하 값이 들어온 경우 예외처리 필요. 나중에 통일시키겠습니다.
        // 또한 mysql에는 Valid의 default값이 true로 잘 설정되는데, h2 테스트 시에만 false로 출력됩니다.
        // 그래서 일단 서비스 단에서 true로 초기화 하도록 하겠습니다.
        Coupon coupon = modelMapper.map(requestCouponDto, Coupon.class);
        coupon.setCouponCode(UUID.randomUUID().toString());
        coupon.setCouponValid(true);
        coupon.setCouponValidDays(requestCouponDto.getCouponValidDays());
        return ResponseCouponDto.from((Coupon) couponRepository.save(coupon));
//        return CouponDto.from(couponRepository.save(coupon));
    }

    @Override
    public ResponseCouponDto getCouponByCouponCode(String couponCode) {
        return ResponseCouponDto.from((Coupon) couponRepository.findByCouponCode(couponCode).orElse(null));
    }

    //쿠폰 삭제
    @Override
    @Transactional
    public void deleteCoupon(Long couponId) throws IdNotExistException{
//        Coupon coupon = (Coupon) couponRepository
//                .findByCouponId(couponId)
//                .orElseThrow(() -> new IdNotExistException("존재하지 않는 쿠폰 번호입니다", ResultCode.ID_NOT_EXIST));
        couponRepository.deleteByCouponId(couponId);
    }

}
