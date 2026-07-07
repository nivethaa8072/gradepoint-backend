package com.example.Gradepoint.repository;

import com.example.Gradepoint.Entity.Attendance;
import com.example.Gradepoint.Entity.Subject;
import com.example.Gradepoint.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

     List<Attendance> findByStudent(User student); 
    Optional<Attendance> findByStudentAndSubject(User student, Subject subject);
}
 