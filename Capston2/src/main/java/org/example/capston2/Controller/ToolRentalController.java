package org.example.capston2.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.capston2.Api.ApiResponse;
import org.example.capston2.Model.ToolRental;
import org.example.capston2.Service.ToolRentalService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
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

    @PostMapping("/rent")
    public ResponseEntity<?> rentToolkit(@RequestBody @Valid ToolRental toolRental, Errors errors){
        if(errors.hasErrors()){
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(message);
        }
        toolRentalService.rentToolkit(toolRental);
        return ResponseEntity.status(200).body(new ApiResponse("ToolKit rented successfully"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateToolRental(@PathVariable Integer id,@RequestBody @Valid ToolRental toolRental, Errors errors){
        if(errors.hasErrors()){
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(message);
        }
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

    @PutMapping("/refund-insurance-fee/{id}/{amount}")
    public ResponseEntity<?> refundInsuranceFee(@PathVariable Integer id,@PathVariable Double amount){
        toolRentalService.refundInsuranceFee(id, amount);
        return ResponseEntity.status(200).body(new ApiResponse("Insurance fee refund success"));
    }

    @GetMapping("/user-upcoming/{userId}")
    public ResponseEntity<?> userUpcomingRentals(@PathVariable Integer userId){
        return ResponseEntity.status(200).body(toolRentalService.userUpcomingRentals(userId));
    }
}
