package org.server.remoteclass.jpa;

import org.server.remoteclass.entity.RateDiscount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RateDiscountRepository extends JpaRepository<RateDiscount, Long> {
    Optional<RateDiscount> findByCouponId(Long couponId);
    Optional<RateDiscount> findByCouponCode(String couponCode);
    List<RateDiscount> findAll();
    void deleteByCouponId(Long couponId);
}
