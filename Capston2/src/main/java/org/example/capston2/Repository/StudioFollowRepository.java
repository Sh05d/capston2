package org.example.capston2.Repository;

import org.example.capston2.Model.StudioFollow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudioFollowRepository extends JpaRepository<StudioFollow,Integer> {
    StudioFollow findStudioFollowById(Integer id);

    @Query("select s from StudioFollow s where s.userId=?1")
    List<StudioFollow> getUserFollows(Integer userId);

    @Query("select s from StudioFollow s where s.studioId=?1 and s.notify=TRUE")
    List<StudioFollow> studioFollowersWithNotification(Integer studioId);
}
