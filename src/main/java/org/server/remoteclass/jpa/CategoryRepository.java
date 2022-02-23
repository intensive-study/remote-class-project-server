package org.server.remoteclass.jpa;

import org.server.remoteclass.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
