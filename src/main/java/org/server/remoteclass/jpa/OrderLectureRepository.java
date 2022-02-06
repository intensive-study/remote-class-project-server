package org.server.remoteclass.jpa;

import org.server.remoteclass.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderLectureRepository extends JpaRepository<Order, Long> {

}
