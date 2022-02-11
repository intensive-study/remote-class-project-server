package org.server.remoteclass.jpa;

import org.server.remoteclass.dto.CouponDto;
import org.server.remoteclass.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
    Optional<Coupon> findByCouponId(Long couponId);
    Optional<Coupon> findByCouponCode(String couponCode);
    List<Coupon> findAll();
    void deleteByCouponId(Long couponId);
}
