package org.server.remoteclass.jpa;

import org.server.remoteclass.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
//    Category findByCategoryName(String categoryName);
}
