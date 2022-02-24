package org.server.remoteclass.jpa;

import org.server.remoteclass.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Long> {

    //수강생 별 장바구니에 넣은 강좌 리스트 최근순으로
    @Query("select c from Cart c " +
            "join fetch c.lecture l " +
            "join fetch l.user " +
            "where c.user.userId=:userId")
    List<Cart> findByUser_UserIdOrderByCreatedDateDesc(Long userId);
    //수강생이 강좌를 수강했었는지 여부
    boolean existsByLecture_LectureIdAndUser_UserId(Long lectureId, Long userId);
    //강의 아이디로 삭제
    void deleteByLecture_LectureIdAndUser_UserId(Long lectureId, Long userId);
    // 장바구니 전체 삭제
    void deleteAllByUser_UserId(Long userId);
    boolean existsByUser_UserId(Long userId);

    @Query("select sum(l.price) from Cart c " +
            "join c.lecture l " +
            "where c.user.userId=:userId")
    int findSumCartByUserId(Long userId);

    @Query("select count(c) from Cart c " +
            "where c.user.userId=:userId")
    int findCountCartByUserId(Long userId);

}