package org.server.remoteclass.service;

import org.server.remoteclass.dto.OrderDto;
import org.server.remoteclass.dto.OrderLectureDto;
import org.server.remoteclass.entity.Lecture;
import org.server.remoteclass.entity.Order;
import org.server.remoteclass.entity.OrderLecture;
import org.server.remoteclass.exception.IdNotExistException;

import java.util.List;

public interface OrderService {


    //사용자 아이디로 해당 주문목록 조회
    List<OrderDto> getOrdersByUserId() throws IdNotExistException;
}
