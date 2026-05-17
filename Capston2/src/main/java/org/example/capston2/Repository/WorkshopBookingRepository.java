package org.example.capston2.Repository;

import org.example.capston2.Model.WorkshopBooking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkshopBookingRepository extends JpaRepository<WorkshopBooking,Integer> {
    WorkshopBooking findWorkshopBookingById(Integer id);

    List<WorkshopBooking> findWorkshopBookingByWorkshopId(Integer workshopId);

    @Query("select b from WorkshopBooking b where b.userId=?1 and b.status='CONFIRM'")
    List<WorkshopBooking> userConfirmBookings(Integer userId);

    List<WorkshopBooking> findWorkshopBookingByUserId(Integer userId);
}
