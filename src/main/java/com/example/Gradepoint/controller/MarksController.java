package com.example.Gradepoint.controller;

import com.example.Gradepoint.Entity.Marks;
import com.example.Gradepoint.Entity.User;
import com.example.Gradepoint.repository.MarksRepository;
import com.example.Gradepoint.repository.UserRepository;
import com.example.Gradepoint.service.PredictionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/marks")
public class MarksController {

    private final MarksRepository marksRepository;
    private final UserRepository userRepository;
    private final PredictionService predictionService;

    public MarksController(MarksRepository marksRepository, UserRepository userRepository, PredictionService predictionService) {
        this.marksRepository = marksRepository;
        this.userRepository = userRepository;
        this.predictionService = predictionService;
    }

    @GetMapping
    public ResponseEntity<List<Marks>> getAllMarks() {
        List<Marks> marks = marksRepository.findAll();
        return ResponseEntity.ok(marks);
    }

    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Marks> addMarks(@RequestBody Marks marks) {
        if (marks.getDate() == null) {
            marks.setDate(LocalDate.now());
        }

        Marks saved = marksRepository.save(marks);

        predictionService.generatePrediction(marks.getStudent(), marks.getSubject());

        return ResponseEntity.ok(saved);
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Marks>> getMarksByStudent(@PathVariable Long studentId) {
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Marks> marks = marksRepository.findByStudent(student);
        return ResponseEntity.ok(marks);
    }
}