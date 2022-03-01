package org.server.remoteclass.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.server.remoteclass.constant.OrderStatus;
import org.server.remoteclass.constant.Payment;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Table(name = "orders")
@Entity
@Getter
@Setter
@NoArgsConstructor
@DynamicInsert
@EntityListeners(AuditingEntityListener.class)
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
    @Column(columnDefinition = "varchar(32) default 'PENDING'")
    private OrderStatus orderStatus = OrderStatus.PENDING; //주문상태

    @Column(name="order_date", nullable = false)
    @CreatedDate
    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private Payment payment; //결제방법

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String bank;  //입금은행

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String account;  //예금주

    @OneToOne
    @JoinColumn(name = "issuedCoupon_id")
    private IssuedCoupon issuedCoupon;       //적용하는 쿠폰 아이디

    @Column(name="original_price")
    private Integer originalPrice;

    @Column(name="sale_price")
    private Integer salePrice;

}
