package org.server.remoteclass.jpa;

import org.server.remoteclass.entity.RateDiscount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RateDiscountRepository extends JpaRepository<RateDiscount, Long> {
}
