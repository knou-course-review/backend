package knou.course.domain.review;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    Page<Review> findAllByCourseId(@Param("courseId") Long courseId, Pageable pageable);

    Page<Review> findAllByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT r.courseId, COUNT(*) " +
            "FROM Review r " +
            "WHERE r.courseId In :courseIds " +
            "GROUP BY r.courseId")
    List<Object[]> countReviewByCourseIds(@Param("courseIds") List<Long> courseIds);
}
