package org.server.remoteclass.jpa;

import org.server.remoteclass.entity.FixDiscount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FixDiscountRepository extends JpaRepository<FixDiscount, Long> {
    Optional<FixDiscount> findByCouponId(Long couponId);
    Optional<FixDiscount> findByCouponCode(String couponCode);
    List<FixDiscount> findAll();
    void deleteByCouponId(Long couponId);
}
