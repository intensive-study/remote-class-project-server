package org.server.remoteclass.entity;

import lombok.*;
import org.server.remoteclass.constant.OrderStatus;
import org.server.remoteclass.constant.Payment;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Table(name = "orders")
@Entity
@Getter
@Setter

public class Order {

    @Id
    @Column(name = "order_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;       //주문하는 회원

    @OneToMany(mappedBy = "order")
    private List<OrderLecture> orderLectures = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus; //주문상태

    @Column(name="order_date", nullable = false)
    private LocalDateTime orderDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="coupon_id")
    private Coupon coupon;       //적용하는 쿠폰 아이디


    private String bank;  //입금은행
    private String account;  //예금주

    @Enumerated(EnumType.STRING)
    private Payment payment; //결제방법

    public void addOrderLecture(OrderLecture orderLecture) {
        orderLectures.add(orderLecture);
        orderLecture.setOrder(this);
    }

    public static Order createOrder(User user, List<OrderLecture> orderLectureList) {
        Order order = new Order();
        order.setUser(user);

        for(OrderLecture orderLecture : orderLectureList) {
            order.addOrderLecture(orderLecture);
        }

        order.setOrderStatus(OrderStatus.PENDING);
        order.setOrderDate(LocalDateTime.now());

        return order;
    }

    public int getTotalPrice() {
        int totalPrice = 0;
        for(OrderLecture orderLecture : orderLectures){
            totalPrice += orderLecture.getLecture().getPrice();
        }
        return totalPrice;
    }

    public void cancelOrder() {
        this.orderStatus = OrderStatus.CANCEL;
        for (OrderLecture orderLecture : orderLectures) {
            orderLecture.cancel();
        }
    }
}
