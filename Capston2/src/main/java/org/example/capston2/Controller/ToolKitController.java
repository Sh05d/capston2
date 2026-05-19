package org.example.capston2.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.capston2.Api.ApiResponse;
import org.example.capston2.Model.ToolKit;
import org.example.capston2.Service.ToolKitService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/toolkit")
@RequiredArgsConstructor
public class ToolKitController {
    private final ToolKitService toolKitService;

    @GetMapping("/get")
    public ResponseEntity<?> getToolKits(){
        return ResponseEntity.status(200).body(toolKitService.getToolKits());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addToolKit(@RequestBody @Valid ToolKit toolKit){
        toolKitService.addToolKit(toolKit);
        return ResponseEntity.status(200).body(new ApiResponse("Toolkit added successfully"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateToolKit(@PathVariable Integer id, @RequestBody @Valid ToolKit toolKit){
        toolKitService.updateToolKit(id, toolKit);
        return ResponseEntity.status(200).body(new ApiResponse("Toolkit updated successfully"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteToolKit(@PathVariable Integer id){
        toolKitService.deleteToolkit(id);
        return ResponseEntity.status(200).body(new ApiResponse("Toolkit deleted successfully"));
    }

    @GetMapping("/by-category-and-price/{category}/{maxPrice}")
    public ResponseEntity<?> toolKitByCategoryAndPriceRange(@PathVariable String category, @PathVariable Integer maxPrice){
        return ResponseEntity.status(200).body(toolKitService.toolKitByCategoryAndPriceRange(category, maxPrice));
    }

    @GetMapping("/by-category-and-quantity/{category}/{minQuantity}")
    public ResponseEntity<?> toolKitByCategoryAndQuantity(@PathVariable String category, @PathVariable Integer minQuantity){
        return ResponseEntity.status(200).body(toolKitService.toolKitByCategoryAndQuantity(category, minQuantity));
    }

    @GetMapping("/by-category-and-pickup-method/{category}/{pickupMethod}")
    public ResponseEntity<?> toolKitByCategoryAndPickupMethod(@PathVariable String category, @PathVariable String pickupMethod){
        return ResponseEntity.status(200).body(toolKitService.toolKitByCategoryAndPickupMethod(category, pickupMethod));
    }

    @GetMapping("/available/quantity/{id}/{firstDate}/{secondDate}")
    public ResponseEntity<?> availableQuantityInDate(@PathVariable Integer id, @PathVariable LocalDate firstDate, @PathVariable LocalDate secondDate){
        return ResponseEntity.status(200).body("Available quantity to rent is "+toolKitService.availableQuantityInDate(id, firstDate, secondDate));
    }
}
