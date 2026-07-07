package com.example.Gradepoint.controller;

import com.example.Gradepoint.Entity.Attendance;
import com.example.Gradepoint.repository.AttendanceRepository;
import com.example.Gradepoint.service.PredictionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    private final AttendanceRepository attendanceRepository;
    private final PredictionService predictionService;

    public AttendanceController(AttendanceRepository attendanceRepository, PredictionService predictionService) {
        this.attendanceRepository = attendanceRepository;
        this.predictionService = predictionService;
    }

    @GetMapping
    public ResponseEntity<List<Attendance>> getAllAttendance() {
        List<Attendance> attendanceList = attendanceRepository.findAll();
        return ResponseEntity.ok(attendanceList);
    }

    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Attendance> addAttendance(@RequestBody Attendance attendance) {
        Attendance saved = attendanceRepository.save(attendance);

        predictionService.generatePrediction(attendance.getStudent(), attendance.getSubject());

        return ResponseEntity.ok(saved);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAttendance(@PathVariable Long id) {
        attendanceRepository.deleteById(id);
        return ResponseEntity.ok("Attendance deleted successfully.");
    }
}