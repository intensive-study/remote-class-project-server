package org.server.remoteclass.jpa;

import org.server.remoteclass.entity.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LectureRepository extends JpaRepository<Lecture, Long>{

    @Query("select l from Lecture l " +
            "join fetch l.category " +
            "join fetch l.user " +
            "where l.lectureId=:lectureId")
    Optional<Lecture> findById(Long lectureId);

    //카테고리별 강의 조회
    @Query("select l from Lecture l " +
            "join fetch l.category " +
            "join fetch l.user " +
            "where l.category.categoryId=:categoryId")
    List<Lecture> findByCategory_CategoryId(Long categoryId);

    @Query("select l from Lecture l " +
            "join fetch l.category " +
            "join fetch l.user")
    List<Lecture> findAll();

}
