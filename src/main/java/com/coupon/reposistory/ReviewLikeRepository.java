package com.coupon.reposistory;

import com.coupon.entity.ReviewLikeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewLikeRepository extends JpaRepository<ReviewLikeEntity, Integer> {

    Optional<ReviewLikeEntity> findByReviewIdAndUserId(Integer reviewId, Integer userId);

}
