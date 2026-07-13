package com.sravani.studentmanagement.service;

import com.sravani.studentmanagement.entity.Attendance;
import com.sravani.studentmanagement.repository.AttendanceRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;

    public AttendanceService(AttendanceRepository attendanceRepository) {
        this.attendanceRepository = attendanceRepository;
    }

    public Attendance saveAttendance(Attendance attendance) {
        return attendanceRepository.save(attendance);
    }

    public List<Attendance> getAllAttendance() {
        return attendanceRepository.findAll();
    }

    public void deleteAttendance(Long id) {
        attendanceRepository.deleteById(id);
    }

}