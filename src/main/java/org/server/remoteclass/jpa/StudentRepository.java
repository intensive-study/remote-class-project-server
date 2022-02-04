package org.server.remoteclass.jpa;

import org.server.remoteclass.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;

public interface StudentRepository extends JpaRepository<Student, Long> {

    // 강좌별 수강생 전체 리스트
    @Query(value = "select * from student s where s.lecture_id = :lectureId", nativeQuery = true)
    Collection<Student> findByLectureId(Long lectureId);

    //수강생 별 수강강좌 리스트
    @Query(value = "select * from student s where s.user_id = :userId", nativeQuery = true)
    Collection<Student> findByUserId(Long userId);

}