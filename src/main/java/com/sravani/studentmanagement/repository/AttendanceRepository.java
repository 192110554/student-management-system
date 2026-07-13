package com.sravani.studentmanagement.repository;

import com.sravani.studentmanagement.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

}