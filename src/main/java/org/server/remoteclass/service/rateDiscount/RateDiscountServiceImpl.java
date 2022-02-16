package org.server.remoteclass.service.rateDiscount;

import org.modelmapper.ModelMapper;
import org.server.remoteclass.dto.rateDiscount.RequestRateDiscountDto;
import org.server.remoteclass.entity.RateDiscount;
import org.server.remoteclass.jpa.RateDiscountRepository;
import org.server.remoteclass.util.BeanConfiguration;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class RateDiscountServiceImpl implements RateDiscountService {

    private final ModelMapper modelMapper;
    private final RateDiscountRepository rateDiscountRepository;

    public RateDiscountServiceImpl(BeanConfiguration beanConfiguration,
                                   RateDiscountRepository rateDiscountRepository){
        this.modelMapper = beanConfiguration.modelMapper();
        this.rateDiscountRepository = rateDiscountRepository;
    }

    @Override
    public void createRateDiscountCoupon(RequestRateDiscountDto requestRateDiscountDto) {
        RateDiscount rateDiscount = modelMapper.map(requestRateDiscountDto, RateDiscount.class);
        rateDiscount.setCouponCode(UUID.randomUUID().toString());
        rateDiscount.setCouponValid(true);
        rateDiscount.setCouponValidDays(requestRateDiscountDto.getCouponValidDays());
        rateDiscount.setDiscountRate(rateDiscount.getDiscountRate());
        rateDiscountRepository.save(rateDiscount);
    }
}
