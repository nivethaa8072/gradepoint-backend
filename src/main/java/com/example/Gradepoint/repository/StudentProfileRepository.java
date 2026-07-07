package com.example.Gradepoint.repository;

import com.example.Gradepoint.Entity.StudentProfile;
import com.example.Gradepoint.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentProfileRepository extends JpaRepository<StudentProfile, Long> {
    Optional<StudentProfile> findByUser(User user);
}