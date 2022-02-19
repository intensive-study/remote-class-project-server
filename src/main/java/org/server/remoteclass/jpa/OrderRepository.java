package org.server.remoteclass.jpa;

import org.server.remoteclass.entity.Order;
import org.server.remoteclass.entity.OrderLecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.Nullable;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    //주문 전체 목록
    List<Order> findAll();

    // 회원이 주문한 주문 목록 최신순으로
    List<Order> findByUser_UserIdOrderByOrderDateDesc(Long userId);

    List<Order> findByOrderByOrderDateDesc();

    @Query("SELECT SUM(l.price) from OrderLecture o, Lecture l where o.lecture.lectureId= l.lectureId and o.order.orderId= :orderId")
    Integer findSumOrderByOrderId(Long orderId);

}
