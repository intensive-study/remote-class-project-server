package org.server.remoteclass.jpa;

import org.server.remoteclass.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Long> {

    //수강생이 강좌를 수강했었는지 여부
    boolean existsByLecture_LectureIdAndUser_UserId(Long lectureId, Long userId);
    //강의 아이디로 삭제
    void deleteByLecture_LectureIdAndUser_UserId(Long lectureId, Long userId);
    // 장바구니 전체 삭제
    void deleteAllByUser_UserId(Long userId);
    boolean existsByUser_UserId(Long userId);

}