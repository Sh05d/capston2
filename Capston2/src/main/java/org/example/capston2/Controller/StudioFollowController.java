package org.example.capston2.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.capston2.Api.ApiResponse;
import org.example.capston2.Model.StudioFollow;
import org.example.capston2.Service.StudioFollowService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/studio-follow")
@RequiredArgsConstructor
public class StudioFollowController {
    private final StudioFollowService studioFollowService;

    @GetMapping("/get")
    public ResponseEntity<?> getStudioFollows(){
        return ResponseEntity.status(200).body(studioFollowService.getStudioFollows());
    }

    @PostMapping("/{userId}/follow/{studioId}")
    public ResponseEntity<?> addStudioFollow(@PathVariable Integer userId,@PathVariable Integer studioId){
        studioFollowService.addStudioFollow(userId,studioId);
        return ResponseEntity.status(200).body(new ApiResponse("Follow success"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateStudioFollow(@PathVariable Integer id, @RequestBody @Valid StudioFollow studioFollow){
        studioFollowService.updateStudioFollow(id, studioFollow);
        return ResponseEntity.status(200).body(new ApiResponse("Follow update success"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteStudioFollow(@PathVariable Integer id){
        studioFollowService.deleteStudioFollow(id);
        return ResponseEntity.status(200).body(new ApiResponse("Unfollow success"));
    }

    @GetMapping("/user-following/{userId}")
    public ResponseEntity<?> userFollowing(@PathVariable Integer userId){
       return ResponseEntity.status(200).body(studioFollowService.userFollowing(userId));
    }

    @PutMapping("/disable-all-notification/{userId}")
    public ResponseEntity<?> disableAllNotification(@PathVariable Integer userId){
        studioFollowService.disableAllNotification(userId);
        return ResponseEntity.status(200).body(new ApiResponse("Disable all notification of following studio success"));
    }
}
