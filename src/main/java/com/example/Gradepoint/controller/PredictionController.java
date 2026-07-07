package com.example.Gradepoint.controller;

import com.example.Gradepoint.Entity.Prediction;
import com.example.Gradepoint.Entity.User;
import com.example.Gradepoint.repository.UserRepository;
import com.example.Gradepoint.service.PredictionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/predictions")
public class PredictionController {

    private final PredictionService predictionService;
    private final UserRepository userRepository;

    public PredictionController(PredictionService predictionService, UserRepository userRepository) {
        this.predictionService = predictionService;
        this.userRepository = userRepository;
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Prediction>> getStudentPredictions(@PathVariable Long studentId) {
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Prediction> predictions = predictionService.getStudentPredictions(student);
        return ResponseEntity.ok(predictions);
    }

    @GetMapping
    public ResponseEntity<List<Prediction>> getAllPredictions() {
        List<Prediction> predictions = predictionService.getAllPredictions();
        return ResponseEntity.ok(predictions);
    }
}