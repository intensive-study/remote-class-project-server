package org.server.remoteclass.jpa;

import org.server.remoteclass.entity.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LectureRepository extends JpaRepository<Lecture, Long>{

    //카테고리별 강의 조회
    List<Lecture> findByCategory_CategoryId(Long categoryId);

}
