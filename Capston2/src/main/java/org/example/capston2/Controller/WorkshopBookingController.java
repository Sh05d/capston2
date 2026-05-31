package org.example.capston2.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.capston2.Api.ApiResponse;
import org.example.capston2.Model.WorkshopBooking;
import org.example.capston2.Service.WorkshopBookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/workshop-booking")
@RequiredArgsConstructor
public class WorkshopBookingController {
    private final WorkshopBookingService workshopBookingService;

    @GetMapping("/get")
    public ResponseEntity<?> getWorkshopBookings(){
        return ResponseEntity.status(200).body(workshopBookingService.getWorkshopBookings());
    }

    @PostMapping("/{userId}/book/{workshopId}")
    public ResponseEntity<?> bookWorkshop(@PathVariable Integer userId, @PathVariable Integer workshopId, @RequestBody @Valid WorkshopBooking workshopBooking){
        workshopBookingService.BookWorkshop(userId, workshopId, workshopBooking);
        return ResponseEntity.status(200).body(new ApiResponse("You booked workshop successfully"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateBooking(@PathVariable Integer id,@RequestBody @Valid WorkshopBooking workshopBooking){
        workshopBookingService.updateWorkshopBooking(id, workshopBooking);
        return ResponseEntity.status(200).body(new ApiResponse("workshop booking updated successfully"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteBooking(@PathVariable Integer id){
        workshopBookingService.deleteWorkshopBooking(id);
        return ResponseEntity.status(200).body(new ApiResponse("workshop booking deleted successfully"));
    }

    @PutMapping("/cancel-booking/{id}")
    public ResponseEntity<?> cancelBooking(@PathVariable Integer id){
        workshopBookingService.cancelBooking(id);
        return ResponseEntity.status(200).body(new ApiResponse("booking canceled successfully"));
    }
}
