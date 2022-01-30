package org.server.remoteclass.entity;


import lombok.*;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Data
@Entity
@Builder
@Table(name="category")
@DynamicInsert
@NoArgsConstructor
@AllArgsConstructor
public class CategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="category_id")
    private Long categoryId;

    @Column(name="category_name", nullable = false, unique = true)
    private String categoryName;
}
