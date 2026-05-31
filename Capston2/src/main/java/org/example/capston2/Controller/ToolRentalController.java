package org.example.capston2.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.capston2.Api.ApiResponse;
import org.example.capston2.Model.ToolRental;
import org.example.capston2.Service.ToolRentalService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/rent-toolkit")
@RequiredArgsConstructor
public class ToolRentalController {
    private final ToolRentalService toolRentalService;

    @GetMapping("/get")
    public ResponseEntity<?> getRentToolKits(){
        return ResponseEntity.status(200).body(toolRentalService.getRentToolKits());
    }

    @PostMapping("/{userId}/rent/{toolKitId}")
    public ResponseEntity<?> rentToolkit(@PathVariable Integer userId, @PathVariable Integer toolKitId ,@RequestBody @Valid ToolRental toolRental){
        toolRentalService.rentToolkit(userId, toolKitId, toolRental);
        return ResponseEntity.status(200).body(new ApiResponse("ToolKit rented successfully"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateToolRental(@PathVariable Integer id,@RequestBody @Valid ToolRental toolRental){
        toolRentalService.updateToolRental(id, toolRental);
        return ResponseEntity.status(200).body(new ApiResponse("Tool renal updated successfully"));
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<?> deleteToolRental(@PathVariable Integer id){
        toolRentalService.deleteToolRental(id);
        return ResponseEntity.status(200).body(new ApiResponse("Tool renal deleted successfully"));
    }

    @PutMapping("cancel-rent/{id}")
    public ResponseEntity<?> cancelRent(@PathVariable Integer id){
        toolRentalService.cancelRent(id);
        return ResponseEntity.status(200).body(new ApiResponse("rent canceled successfully"));
    }

    @PutMapping("handover-tool/{id}")
    public ResponseEntity<?> handoverTool(@PathVariable Integer id){
        toolRentalService.handoverTool(id);
        return ResponseEntity.status(200).body(new ApiResponse("rent status change to handover successfully"));
    }

    @PutMapping("returned-tool/{id}")
    public ResponseEntity<?> returnedTool(@PathVariable Integer id){
        toolRentalService.returnedTool(id);
        return ResponseEntity.status(200).body(new ApiResponse("rent canceled status change to returned successfully"));
    }

    @PutMapping("/mark-damage/{id}/{damageCost}")
    public ResponseEntity<?> markDamage(@PathVariable Integer id, @PathVariable Double damageCost){
        toolRentalService.markDamage(id, damageCost);
        return ResponseEntity.status(200).body(new ApiResponse("mark damage success"));
    }

    @PutMapping("/refund-deposit/{id}")
    public ResponseEntity<?> refundInsuranceFee(@PathVariable Integer id){
        toolRentalService.refundDeposit(id);
        return ResponseEntity.status(200).body(new ApiResponse("deposit refunded success"));
    }

    @GetMapping("/user-upcoming/{userId}")
    public ResponseEntity<?> userUpcomingRentals(@PathVariable Integer userId){
        return ResponseEntity.status(200).body(toolRentalService.userUpcomingRentals(userId));
    }
}
