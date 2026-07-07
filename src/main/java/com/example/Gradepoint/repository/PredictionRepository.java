package com.example.Gradepoint.repository;

import com.example.Gradepoint.Entity.Prediction;
import com.example.Gradepoint.Entity.Subject;
import com.example.Gradepoint.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PredictionRepository extends JpaRepository<Prediction, Long> {
    List<Prediction> findByStudent(User student);
    Optional<Prediction> findByStudentAndSubject(User student, Subject subject);
}