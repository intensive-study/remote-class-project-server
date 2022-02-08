package org.server.remoteclass.jpa;

import org.server.remoteclass.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderLectureRepository extends JpaRepository<Order, Long> {

    //강의 가격 합
//    @Query(value = "select sum(l.price) from OrderLecture o, Lecture l where o.lectureId = l.lectureId and o.orderId = :orderId", nativeQuery = true)
//    Double findSumOrdersByOrderId(Long orderId);



}
