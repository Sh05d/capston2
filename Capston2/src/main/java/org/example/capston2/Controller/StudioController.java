package org.example.capston2.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.capston2.Api.ApiResponse;
import org.example.capston2.Model.Studio;
import org.example.capston2.Service.StudioService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/studio")
@RequiredArgsConstructor
public class StudioController {
    private final StudioService studioService;

    @GetMapping("/get")
    public ResponseEntity<?> getStudios(){
        return ResponseEntity.status(200).body(studioService.getStudios());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addStudio(@RequestBody @Valid Studio studio, Errors errors){
        if(errors.hasErrors()){
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(message);
        }
        studioService.addStudio(studio);
        return ResponseEntity.status(200).body(new ApiResponse("Studio added successfully"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateStudio(@PathVariable Integer id, @RequestBody @Valid Studio studio, Errors errors){
        if(errors.hasErrors()){
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(message);
        }
        studioService.updateStudio(id, studio);
        return ResponseEntity.status(200).body(new ApiResponse("Studio updated successfully"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteStudio(@PathVariable Integer id){
        studioService.deleteStudio(id);
        return ResponseEntity.status(200).body(new ApiResponse("Studio deleted successfully"));
    }

    @GetMapping("/best-in-city/{city}")
    public ResponseEntity<?> bestStudioInCity(@PathVariable String city){
        return ResponseEntity.status(200).body(studioService.bestStudioInCity(city));
    }

}
