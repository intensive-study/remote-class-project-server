package org.server.remoteclass.jpa;

import org.server.remoteclass.entity.LectureEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LectureRepository extends JpaRepository<LectureEntity, Long>{
    LectureEntity findLectureEntityByLectureId(Long lectureId);
    LectureEntity findByLectureId(Long lectureId);

}
