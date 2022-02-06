package org.server.remoteclass.jpa;

import org.server.remoteclass.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    //주문 전체 목록
    List<Order> findAll();

    // 회원이 주문한 주문 목록 최신순으로
    @Query(value = "select o from Order o where o.user.userId = :userId order by o.orderDate desc", nativeQuery = true)
    List<Order> findByUserId(Long userId);

}
