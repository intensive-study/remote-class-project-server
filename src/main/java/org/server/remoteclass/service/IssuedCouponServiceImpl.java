package org.server.remoteclass.service;

import org.modelmapper.ModelMapper;
import org.server.remoteclass.dto.CouponDto;
import org.server.remoteclass.dto.IssuedCouponDto;
import org.server.remoteclass.dto.StudentDto;
import org.server.remoteclass.entity.IssuedCoupon;
import org.server.remoteclass.entity.User;
import org.server.remoteclass.exception.IdNotExistException;
import org.server.remoteclass.exception.ResultCode;
import org.server.remoteclass.jpa.IssuedCouponRepository;
import org.server.remoteclass.jpa.UserRepository;
import org.server.remoteclass.util.BeanConfiguration;
import org.server.remoteclass.util.SecurityUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class IssuedCouponServiceImpl implements IssuedCouponService{

    private final IssuedCouponRepository issuedCouponRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public IssuedCouponServiceImpl(IssuedCouponRepository issuedCouponRepository,
                                   BeanConfiguration beanConfiguration,
                                   UserRepository userRepository) {
        this.issuedCouponRepository = issuedCouponRepository;
        this.modelMapper = beanConfiguration.modelMapper();
        this.userRepository = userRepository;
    }

    @Override
    public IssuedCouponDto issueCoupon(String couponCode) {
        return null;
    }

    // 테스트 해 봐야 합니다.
    @Override
    public List<IssuedCouponDto> getAllMyCoupons() throws IdNotExistException{
        User user = SecurityUtil.getCurrentUserEmail()
                .flatMap(userRepository::findByEmail)
                .orElseThrow(() -> new IdNotExistException("존재하지 않는 사용자", ResultCode.ID_NOT_EXIST));

        List<IssuedCoupon> issuedCouponDtoList = issuedCouponRepository.findByUser(user.getUserId());
        return issuedCouponDtoList.stream().map(issuedCoupon -> modelMapper.map(issuedCoupon, IssuedCouponDto.class)).collect(Collectors.toList());
    }
}
