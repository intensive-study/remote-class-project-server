package org.server.remoteclass.jpa;

import org.server.remoteclass.entity.IssuedCoupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IssuedCouponRepository extends JpaRepository<IssuedCoupon, Long> {
    IssuedCoupon findByIssuedCouponId(Long issuedCouponId);

    // 내가 가지고 있는 쿠폰 조회 기능
    @Query("select i from IssuedCoupon i " +
            "join fetch i.coupon " +
            "where i.user.userId=:userId")
    List<IssuedCoupon> findByUser(@Param("userId") Long userId);

    // 쿠폰 발급번호와 사용자 아이디로 쿠폰 찾기
    @Query("select i from IssuedCoupon i " +
            "join fetch i.coupon " +
            "where i.user.userId=:userId and i.issuedCouponId = :issuedCouponId")
    Optional<IssuedCoupon> findByUserAndIssuedCouponId(@Param("userId") Long userId, @Param("issuedCouponId") Long issuedCouponId);
}
