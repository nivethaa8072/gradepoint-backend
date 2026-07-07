package com.example.Gradepoint.controller;

import com.example.Gradepoint.Entity.StudentProfile;
import com.example.Gradepoint.Entity.User;
import com.example.Gradepoint.repository.StudentProfileRepository;
import com.example.Gradepoint.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final UserRepository userRepository;
    private final StudentProfileRepository studentProfileRepository;

    public StudentController(UserRepository userRepository, StudentProfileRepository studentProfileRepository) {
        this.userRepository = userRepository;
        this.studentProfileRepository = studentProfileRepository;
    }

    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<User>> getAllStudents() {
        List<User> students = userRepository.findByRole("ROLE_STUDENT");
        return ResponseEntity.ok(students);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getStudentById(@PathVariable Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Optional<StudentProfile> profile = studentProfileRepository.findByUser(user);

        Map<String, Object> response = new HashMap<>();
        response.put("user", user);
        response.put("profile", profile.orElse(null));

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('STUDENT') or hasRole('ADMIN')")
    @PostMapping("/{id}/profile")
    public ResponseEntity<StudentProfile> createOrUpdateProfile(@PathVariable Long id, @RequestBody StudentProfile studentProfile) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        studentProfile.setUser(user);
        StudentProfile saved = studentProfileRepository.save(studentProfile);

        return ResponseEntity.ok(saved);
    }
}