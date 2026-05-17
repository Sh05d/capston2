package org.example.capston2.Controller;

import lombok.RequiredArgsConstructor;
import org.example.capston2.Service.AIService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/ai")
@RequiredArgsConstructor
public class AIController {
    private final AIService aiService;

    @GetMapping("/workshop-recommend/{userId}")
    public ResponseEntity<?> recommendWorkshops(@PathVariable Integer userId){
        return ResponseEntity.status(200).body(aiService.recommendWorkshops(userId));
    }

    @GetMapping("/toolkit-recommend/{userId}")
    public ResponseEntity<?> recommendToolkits(@PathVariable Integer userId){
        return ResponseEntity.status(200).body(aiService.recommendToolkits(userId));
    }
}

