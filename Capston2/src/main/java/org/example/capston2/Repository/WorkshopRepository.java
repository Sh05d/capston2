package org.example.capston2.Repository;

import org.example.capston2.Model.Workshop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkshopRepository extends JpaRepository<Workshop,Integer> {
    Workshop findWorkshopById(Integer id);

    @Query("select w from Workshop w where w.studioId=?1 and w.startDate>CURRENT_TIMESTAMP and w.status!='CANCEL'")
    List<Workshop> upcomingWorkshopForStudio(Integer studioId);

    @Query("select w from Workshop w where w.category=?1 and w.price<=?2 and w.status='SCHEDULE'")
    List<Workshop> availableWorkshopByCategoryAndPriceRange(String category, Integer maxPrice);

    @Query("select w from Workshop w where w.category=?1 and w.audienceGender=?2 and w.status='SCHEDULE'")
    List<Workshop> availableWorkshopByCategoryAndAudience(String category, String audienceGender);

    @Query("select w from Workshop w where w.id=?1 and w.status='SCHEDULE'")
    Workshop getActiveWorkshopById(Integer id);

    @Query("select w from Workshop w where w.id=?1 and w.studioId=?2 and w.status='COMPLETE'")
    Workshop completeWorkshopByStudioId(Integer id, Integer studioId);
}
