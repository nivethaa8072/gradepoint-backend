package com.example.Gradepoint.repository;

import com.example.Gradepoint.Entity.Marks;
import com.example.Gradepoint.Entity.Subject;
import com.example.Gradepoint.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MarksRepository extends JpaRepository<Marks, Long> {
    List<Marks> findByStudent(User student);
    List<Marks> findByStudentAndSubject(User student, Subject subject);
}
