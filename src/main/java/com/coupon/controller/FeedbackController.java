package com.coupon.controller;

import com.coupon.model.FeedbackDTO;
import com.coupon.model.ReviewDTO;
import com.coupon.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/feedback")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    @PostMapping
    public ResponseEntity<Map<String, String>> saveFeedback(@RequestBody FeedbackDTO feedbackDTO) {
        feedbackService.saveFeedback(feedbackDTO);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Review saved successfully");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Endpoint to get all feedback
    @GetMapping("/list")
    public ResponseEntity<List<FeedbackDTO>> showAllFeedback() {
        List<FeedbackDTO> feedbackList = feedbackService.showAllFeedback();
        return ResponseEntity.ok(feedbackList);
    }

}

