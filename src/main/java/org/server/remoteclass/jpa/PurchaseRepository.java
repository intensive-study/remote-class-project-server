package org.server.remoteclass.jpa;

import org.server.remoteclass.constant.Authority;
import org.server.remoteclass.entity.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
//    @Query("select p from Purchase p where p.order.user.userId = ?1 order by p.purchaseDate DESC")
    List<Purchase> findByOrder_User_UserIdOrderByPurchaseDateDesc(Long userId);
}
