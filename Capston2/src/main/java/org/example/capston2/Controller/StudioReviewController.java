package org.example.capston2.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.capston2.Api.ApiResponse;
import org.example.capston2.Model.StudioReview;
import org.example.capston2.Service.StudioReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/studio-review")
@RequiredArgsConstructor
public class StudioReviewController {
    private final StudioReviewService studioReviewService;

    @GetMapping("/get")
    public ResponseEntity<?> getStudioReview(){
        return ResponseEntity.status(200).body(studioReviewService.getStudioReviews());
    }

    @PostMapping("/{userId}/add=review-on/{studioId}")
    public ResponseEntity<?> addStudioReview(@PathVariable Integer userId,@PathVariable Integer studioId, @RequestBody @Valid StudioReview studioReview){
        studioReviewService.addStudioReview(userId, studioId, studioReview);
        return ResponseEntity.status(200).body(new ApiResponse("Review added successfully"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateStudioReview(@PathVariable Integer id, @RequestBody @Valid StudioReview studioReview){
        studioReviewService.updateStudioReview(id, studioReview);
        return ResponseEntity.status(200).body(new ApiResponse("Review updated successfully"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteStudioReview(@PathVariable Integer id){
        studioReviewService.deleteStudioReview(id);
        return ResponseEntity.status(200).body(new ApiResponse("Review deleted successfully"));

    }

    @GetMapping("/on-studio/{studioId}")
    public ResponseEntity<?> reviewsOnStudio(@PathVariable Integer studioId){
        return ResponseEntity.status(200).body(studioReviewService.studioReviews(studioId));
    }

    @GetMapping("/on-studio-rate/{studioId}/{rate}")
    public ResponseEntity<?> reviewsOnStudioByRate(@PathVariable Integer studioId, @PathVariable Double rate){
        return ResponseEntity.status(200).body(studioReviewService.studioReviewsByRate(studioId, rate));
    }
}
