package org.server.remoteclass.jpa;

import org.server.remoteclass.dto.CouponDto;
import org.server.remoteclass.entity.IssuedCoupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IssuedCouponRepository extends JpaRepository<IssuedCouponRepository, Long> {

    // 내가 가지고 있는 쿠폰 조회 기능
    List<IssuedCoupon> findByUserId(Long userId);
}
