package org.server.remoteclass.jpa;

import org.server.remoteclass.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CouponRepository<T extends Coupon> extends JpaRepository<T, Long> {

    Optional<T> findByCouponId(Long couponId);
    Optional<T> findByCouponCode(String couponCode);
    void deleteByCouponId(Long couponId);
}
