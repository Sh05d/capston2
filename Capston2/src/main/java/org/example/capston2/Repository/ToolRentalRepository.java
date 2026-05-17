package org.example.capston2.Repository;

import org.example.capston2.Model.ToolRental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ToolRentalRepository extends JpaRepository<ToolRental,Integer> {
    ToolRental findRentToolKitById(Integer id);

    @Query("select r from ToolRental r where r.toolKitId=?1 and r.startDate < ?3 and r.endDate > ?2 and r.status!='CANCEL'")
    List<ToolRental> toolRentsAtDate(Integer toolKitId, LocalDateTime startDate, LocalDateTime endDate);

    @Query("select r from ToolRental r where r.userId=?1 and r.startDate>CURRENT_TIMESTAMP and r.status!='CANCEL'")
    List<ToolRental> userUpcomingRentals(Integer userId);

    @Query("select r from ToolRental r where r.userId=?1 and r.status='RETURNED'")
    List<ToolRental> userReturnedRentals(Integer userId);

    List<ToolRental> findToolRentalByUserId(Integer userId);
}
