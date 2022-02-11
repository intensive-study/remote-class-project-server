package org.server.remoteclass.jpa;

import org.server.remoteclass.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {

    // 강좌별 수강생 전체 리스트
    List<Student> findByLecture_LectureId(Long lectureId);
    //수강생 별 수강강좌 리스트
    List<Student> findByUser_UserIdOrderByLecture_StartDateDesc(Long userId);
    //수강생이 강좌를 수강했었는지 여부
    boolean existsByLecture_LectureIdAndUser_UserId(Long lectureId, Long userId);
    //강의 아이디로 삭제
    void deleteByLecture_LectureIdAndUser_UserId(Long lectureId, Long userId);

}