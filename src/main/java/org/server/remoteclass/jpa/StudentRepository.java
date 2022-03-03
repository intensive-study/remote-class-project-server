package org.server.remoteclass.jpa;

import org.server.remoteclass.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {

    // 강좌별 수강생 전체 리스트
    @Query("select s from Student s " +
            "join fetch s.user " +
            "where s.lecture.lectureId=:lectureId")
    List<Student> findByLecture_LectureId(@Param("lectureId") Long lectureId);

    //수강생 별 수강강좌 리스트
    @Query("select s from Student s " +
            "join fetch s.lecture l " +
            "join fetch l.user " +
            "join fetch l.category " +
            "where s.user.userId=:userId " +
            "order by l.startDate desc")
    List<Student> findByUser_UserIdOrderByLecture_StartDateDesc(@Param("userId") Long userId);

    //수강생이 강좌를 수강했었는지 여부
    boolean existsByLecture_LectureIdAndUser_UserId(Long lectureId, Long userId);

    //본인의 주문 아이디로 수강생 모두 삭제
    void deleteByUser_UserIdAndOrder_OrderId(Long userId, Long orderId);

}