package org.example.capston2.Repository;

import org.example.capston2.Model.StudioReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudioReviewRepository extends JpaRepository<StudioReview,Integer> {
    StudioReview findReviewById(Integer id);

    @Query("select r from StudioReview r where r.studioId=?1")
    List<StudioReview> studioReviews(Integer studioId);

    @Query("select r from StudioReview r where r.studioId=?1 and r.rate=?2")
    List<StudioReview> studioReviewsByRate(Integer studioId ,Double rate);
}
