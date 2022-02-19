package org.server.remoteclass.service.fixDiscount;

import org.modelmapper.ModelMapper;
import org.server.remoteclass.dto.fixDiscount.RequestFixDiscountDto;
import org.server.remoteclass.dto.fixDiscount.ResponseFixDiscountDto;
import org.server.remoteclass.entity.FixDiscount;
import org.server.remoteclass.jpa.FixDiscountRepository;
import org.server.remoteclass.util.BeanConfiguration;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class FixDiscountServiceImpl implements FixDiscountService {

    private final ModelMapper modelMapper;
    private final FixDiscountRepository fixDiscountRepository;

    public FixDiscountServiceImpl(BeanConfiguration beanConfiguration,
                                  FixDiscountRepository fixDiscountRepository){
        this.modelMapper = beanConfiguration.modelMapper();
        this.fixDiscountRepository = fixDiscountRepository;
    }

    @Override
    public void createFixDiscountCoupon(RequestFixDiscountDto requestFixDiscountDto) {
        FixDiscount fixDiscount = modelMapper.map(requestFixDiscountDto, FixDiscount.class);
        fixDiscount.setCouponCode(UUID.randomUUID().toString());
        fixDiscount.setCouponValid(true);
        fixDiscount.setCouponValidDays(requestFixDiscountDto.getCouponValidDays());
        fixDiscount.setDiscountPrice(requestFixDiscountDto.getDiscountPrice());
        fixDiscountRepository.save(fixDiscount);
    }

    @Override
    public List<ResponseFixDiscountDto> getAllFixDiscountCoupons() {
        List<FixDiscount> fixDiscountList = fixDiscountRepository.findAll();
        return fixDiscountList.stream()
                .map(fixDiscount ->
                        ResponseFixDiscountDto.from(fixDiscount)
                ).collect(Collectors.toList());
    }

    @Override
    public ResponseFixDiscountDto getFixDiscountCoupon(Long couponId) {
        return ResponseFixDiscountDto.from(fixDiscountRepository.findByCouponId(couponId).orElse(null));
    }
}
