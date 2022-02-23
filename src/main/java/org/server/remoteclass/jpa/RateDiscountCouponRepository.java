package org.server.remoteclass.jpa;

import org.server.remoteclass.entity.RateDiscountCoupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RateDiscountCouponRepository extends JpaRepository<RateDiscountCoupon, Long> {
    Optional<RateDiscountCoupon> findByCouponId(Long couponId);
    Optional<RateDiscountCoupon> findByCouponCode(String couponCode);
    List<RateDiscountCoupon> findAll();
    void deleteByCouponId(Long couponId);

    boolean existsByCouponId(Long couponId);
}
