package org.server.remoteclass.jpa;

import org.server.remoteclass.entity.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {

    @Query("select p from Purchase p " +
            "join fetch p.order o " +
            "where o.user.userId=:userId " +
            "order by p.purchaseDate desc")
    List<Purchase> findByOrder_User_UserIdOrderByPurchaseDateDesc(@Param("userId") Long userId);

    @Query("select p from Purchase p " +
            "join fetch p.order " +
            "where p.purchaseId=:purchaseId")
    Optional<Purchase> findById(@Param("purchaseId") Long purchaseId);

    @Query("select p from Purchase p " +
            "join fetch p.order " +
            "where p.order.orderId=:orderId ")
    Optional<Purchase> findByOrder_OrderId(Long orderId);
}
