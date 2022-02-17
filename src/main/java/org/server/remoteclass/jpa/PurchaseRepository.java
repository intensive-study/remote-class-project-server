package org.server.remoteclass.jpa;

import org.server.remoteclass.constant.Authority;
import org.server.remoteclass.entity.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
    List<Purchase> findByOrder_User_UserIdOrderByPurchaseDateDesc(Long userId);
}
