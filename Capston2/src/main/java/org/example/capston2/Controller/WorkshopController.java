package org.example.capston2.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.capston2.Api.ApiResponse;
import org.example.capston2.Model.Workshop;
import org.example.capston2.Service.WorkshopService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/workshop")
@RequiredArgsConstructor
public class WorkshopController {
    private final WorkshopService workshopService;

    @GetMapping("/get")
    public ResponseEntity<?> getWorkshops(){
        return ResponseEntity.status(200).body(workshopService.getWorkshops());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addWorkshop(@RequestBody @Valid Workshop workshop, Errors errors){
        if(errors.hasErrors()){
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(message);
        }
        workshopService.addWorkshop(workshop);
        return ResponseEntity.status(200).body(new ApiResponse("Workshop added successfully"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateWorkshop(@PathVariable Integer id, @RequestBody @Valid Workshop workshop, Errors errors){
        if(errors.hasErrors()){
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(message);
        }
        workshopService.updateWorkshop(id, workshop);
        return ResponseEntity.status(200).body(new ApiResponse("Workshop updated successfully"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteWorkshop(@PathVariable Integer id){
        workshopService.deleteWorkshop(id);
        return ResponseEntity.status(200).body(new ApiResponse("Workshop deleted successfully"));
    }

    @PutMapping("cancel/{id}")
    public ResponseEntity<?> cancelWorkshop(@PathVariable Integer id){
        workshopService.cancelWorkshop(id);
        return ResponseEntity.status(200).body(new ApiResponse("Workshop canceled successfully"));
    }

    @PutMapping("/complete/{id}")
    public ResponseEntity<?> completeWorkshop(@PathVariable Integer id){
        workshopService.completeWorkshop(id);
        return ResponseEntity.status(200).body(new ApiResponse("Workshop completed"));
    }

    @GetMapping("/upcoming-studio/{studioId}")
    public ResponseEntity<?> upcomingWorkshopForStudio(@PathVariable Integer studioId){
        return ResponseEntity.status(200).body(workshopService.upcomingWorkshopForStudio(studioId));
    }

    @GetMapping("/by-category-and-price/{category}/{maxPrice}")
    public ResponseEntity<?> workshopByCategoryAndPriceRange(@PathVariable String category,@PathVariable Integer maxPrice){
        return ResponseEntity.status(200).body(workshopService.workshopByCategoryAndPriceRange(category, maxPrice));
    }

    @GetMapping("/by-category-and-audience/{category}/{audienceGender}")
    public ResponseEntity<?> workshopByCategoryAndAudience(@PathVariable String category,@PathVariable String audienceGender){
        return ResponseEntity.status(200).body(workshopService.workshopByCategoryAndAudience(category, audienceGender));
    }

    @GetMapping("/by-category-and-date/{category}/{Date}")
    public ResponseEntity<?> workshopByCategoryAndDate(@PathVariable String category, @PathVariable LocalDate Date){
        return ResponseEntity.status(200).body(workshopService.workshopByCategoryAndDate(category, Date));
    }

}
