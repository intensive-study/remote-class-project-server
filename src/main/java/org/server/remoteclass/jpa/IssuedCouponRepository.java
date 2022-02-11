package org.server.remoteclass.jpa;

import org.server.remoteclass.dto.CouponDto;
import org.server.remoteclass.entity.IssuedCoupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IssuedCouponRepository extends JpaRepository<IssuedCoupon, Long> {

    // 내가 가지고 있는 쿠폰 조회 기능
    @Query("SELECT m FROM IssuedCoupon m where m.user.userId = :userId")
    List<IssuedCoupon> findByUser(@Param("userId") Long userId);
    // 쿠폰 발급번호로 쿠폰 찾기
    @Query("SELECT m FROM IssuedCoupon m where m.user.userId = :userId and m.issuedCouponId = :issuedCouponId")
    Optional<IssuedCoupon> findByUserAndIssuedCouponId(@Param("userId") Long userId, @Param("issuedCouponId") Long issuedCouponId);
}
