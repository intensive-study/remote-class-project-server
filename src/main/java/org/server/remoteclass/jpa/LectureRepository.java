package org.server.remoteclass.jpa;

import org.server.remoteclass.entity.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;

public interface LectureRepository extends JpaRepository<Lecture, Long>{

    @Query(value = "select * from lecture l where l.category_id = :categoryId", nativeQuery = true)
    Collection<Lecture> findByCategoryId(Long categoryId);

}
