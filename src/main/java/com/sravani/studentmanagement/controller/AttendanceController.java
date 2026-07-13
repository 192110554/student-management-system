package com.sravani.studentmanagement.controller;

import com.sravani.studentmanagement.service.EmailService;
import com.sravani.studentmanagement.entity.Attendance;
import com.sravani.studentmanagement.entity.Student;
import com.sravani.studentmanagement.repository.StudentRepository;
import com.sravani.studentmanagement.service.AttendanceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;
    private final StudentRepository studentRepository;
    private final EmailService emailService;

    public AttendanceController(AttendanceService attendanceService,
                                StudentRepository studentRepository,
                                EmailService emailService) {

        this.attendanceService = attendanceService;
        this.studentRepository = studentRepository;
        this.emailService = emailService;
    }

    @GetMapping
    public List<Attendance> getAllAttendance() {
        return attendanceService.getAllAttendance();
    }

    @PostMapping("/{studentId}")
    public ResponseEntity<?> markAttendance(
            @PathVariable Long studentId,
            @RequestParam String status) {

        Student student = studentRepository.findById(studentId)
                .orElseThrow();

        Attendance attendance = new Attendance();

        attendance.setAttendanceDate(LocalDate.now());
        attendance.setStatus(status);
        attendance.setStudent(student);

        Attendance savedAttendance = attendanceService.saveAttendance(attendance);

        emailService.sendAttendanceEmail(
                student.getEmail(),
                student.getName(),
                status
        );

        return ResponseEntity.ok(savedAttendance);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAttendance(
            @PathVariable Long id) {

        attendanceService.deleteAttendance(id);

        return ResponseEntity.ok("Attendance Deleted Successfully");
    }
}