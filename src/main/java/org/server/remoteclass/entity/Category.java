package org.server.remoteclass.entity;


import lombok.*;
import javax.persistence.*;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="category_id")
    private Long categoryId;

    @Column(name="category_name", nullable = false, unique = true)
    private String categoryName;
}
