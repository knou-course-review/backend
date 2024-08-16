package knou.course.domain.like;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReviewLikeRepository extends JpaRepository<ReviewLike, Long> {

    boolean existsByUserIdAndReviewId(Long userId, Long reviewId);

    Optional<ReviewLike> findByUserIdAndReviewId(Long userId, Long reviewId);

    @Query("SELECT l.reviewId, COUNT(l.id) " +
            "FROM ReviewLike l " +
            "WHERE l.reviewId In :reviewIds " +
            "GROUP BY l.reviewId")
    List<Object[]> countLikesByReviewIds(@Param("reviewIds") List<Long> reviewIds);

    @Query("SELECT l.reviewId, COUNT(l.id) > 0 " +
            "FROM ReviewLike l " +
            "WHERE l.reviewId In :reviewIds AND l.userId = :userId " +
            "GROUP BY l.reviewId")
    List<Object[]> getUserLikeStatus(List<Long> reviewIds, Long userId);
}
