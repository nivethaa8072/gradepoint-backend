package com.example.Gradepoint.service;

import com.example.Gradepoint.Entity.*;
import com.example.Gradepoint.repository.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PredictionService {

    private final MarksRepository marksRepository;

    private final AttendanceRepository attendanceRepository;

    private final PredictionRepository predictionRepository;

    PredictionService(MarksRepository marksRepository, AttendanceRepository attendanceRepository, PredictionRepository predictionRepository) {
        this.marksRepository = marksRepository;
        this.attendanceRepository = attendanceRepository;
        this.predictionRepository = predictionRepository;
    }

    public void generatePrediction(User student, Subject subject) {

        List<Marks> marksList = marksRepository.findByStudentAndSubject(student, subject);

        if (marksList.isEmpty()) {
            return;
        }

        double averageMarks = marksList.stream()
                .mapToDouble(Marks::getScore)
                .average()
                .orElse(0.0);

        Optional<Attendance> attendanceOpt = attendanceRepository.findByStudentAndSubject(student, subject);

        double attendancePercentage = 0.0;
        if (attendanceOpt.isPresent()) {
            Attendance attendance = attendanceOpt.get();
            if (attendance.getTotalClasses() != null && attendance.getTotalClasses() > 0) {
                attendancePercentage = (attendance.getAttendedClasses() * 100.0) / attendance.getTotalClasses();
            }
        }

        double predictedScore = (averageMarks * 0.7) + (attendancePercentage * 0.3);

        String riskLevel;
        if (predictedScore < 40) {
            riskLevel = "HIGH RISK";
        } else if (predictedScore <= 70) {
            riskLevel = "MEDIUM";
        } else {
            riskLevel = "LOW";
        }

        Prediction prediction = predictionRepository.findByStudentAndSubject(student, subject)
                .orElse(new Prediction());

        prediction.setStudent(student);
        prediction.setSubject(subject);
        prediction.setPredictedScore(predictedScore);
        prediction.setRiskLevel(riskLevel);

        predictionRepository.save(prediction);
    }

    public List<Prediction> getStudentPredictions(User student) {
        return predictionRepository.findByStudent(student);
    }

    public List<Prediction> getAllPredictions() {
        return predictionRepository.findAll();
    }
}