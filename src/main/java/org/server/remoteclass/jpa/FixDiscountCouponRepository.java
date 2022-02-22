package org.server.remoteclass.jpa;

import org.server.remoteclass.entity.FixDiscountCoupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FixDiscountCouponRepository extends JpaRepository<FixDiscountCoupon, Long> {
    Optional<FixDiscountCoupon> findByCouponId(Long couponId);
    Optional<FixDiscountCoupon> findByCouponCode(String couponCode);
    List<FixDiscountCoupon> findAll();
    void deleteByCouponId(Long couponId);

    boolean existsByCouponId(Long couponId);

}
