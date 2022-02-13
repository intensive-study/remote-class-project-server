package org.server.remoteclass.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class OrderLecture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_lecture_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecture_id")
    private Lecture lecture;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    public static OrderLecture createOrderLecture(Lecture lecture){
        OrderLecture orderLecture = new OrderLecture();
        orderLecture.setLecture(lecture);
        return orderLecture;
    }

    public void cancel() {
        this.getLecture();
    }
}
