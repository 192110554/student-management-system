package com.sravani.studentmanagement.repository;

import com.sravani.studentmanagement.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findByEmail(String email);

    List<Student> findByName(String name);

    List<Student> findByCourse(String course);

}