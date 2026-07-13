package com.sravani.studentmanagement.controller;

import com.sravani.studentmanagement.entity.Student;
import com.sravani.studentmanagement.service.StudentService;
import org.springframework.web.bind.annotation.*;
import com.sravani.studentmanagement.entity.Attendance;
import com.sravani.studentmanagement.service.AttendanceService;
import java.time.LocalDate;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class AIController {

    private final StudentService studentService;

    private final AttendanceService attendanceService;

    public AIController(StudentService studentService,
                        AttendanceService attendanceService) {

        this.studentService = studentService;
        this.attendanceService = attendanceService;
    }

    @PostMapping("/ai")
    public String askAI(@RequestBody Map<String, String> request) {

        String question = request.get("question").toLowerCase();

        List<Student> students =
                studentService.getStudents(
                        org.springframework.data.domain.Pageable.unpaged()
                ).getContent();

        if (question.contains("hi")
                || question.contains("hello")
                || question.contains("hey")) {

            return """
            👋 Hello!

            Welcome to Student AI Assistant.

            You can ask me:

            • Total students

            • Java students

            • Spring Boot students

            • List students

            • Search Ramya

            • Today's attendance

            • Present students

            • Absent students
            """;
        }

        if (question.contains("help")) {

            return """
            🤖 I can help you with:

            ✅ Total students

            ✅ Java students

            ✅ Spring Boot students

            ✅ List students

            ✅ Search student

            ✅ Today's attendance

            ✅ Present students

            ✅ Absent students
            """;
        }

        if (question.startsWith("search ") || !question.contains(" ")) {

            String name;

            if (question.startsWith("search")) {

                name = question.replace("search", "").trim();

            } else {

                name = question.trim();

            }

            for (Student student : students) {

                if (student.getName().equalsIgnoreCase(name)) {

                    return """
                    Student Found

                    Name : %s

                    Email : %s

                    Course : %s
                    """.formatted(
                            student.getName(),
                            student.getEmail(),
                            student.getCourse()
                    );

                }

            }

            return "Student not found.";
        }

        if (question.contains("today")) {

            List<Attendance> attendanceList =
                    attendanceService.getAllAttendance();

            StringBuilder builder = new StringBuilder();

            for (Attendance attendance : attendanceList) {

                if (attendance.getAttendanceDate()
                        .equals(LocalDate.now())) {

                    builder.append(attendance.getStudent().getName())

                            .append(" - ")

                            .append(attendance.getStatus())

                            .append("<br>");

                }

            }

            if (builder.isEmpty()) {

                return "No attendance recorded today.";

            }

            return builder.toString();

        }
        // Total students
        if (question.contains("total") || question.contains("how many students")) {

            return "There are " + students.size() + " students in the system.";

        }

        // Java students
        if (question.contains("java")) {

            long count = students.stream()
                    .filter(s -> s.getCourse().equalsIgnoreCase("Java"))
                    .count();

            return "There are " + count + " Java students.";

        }

        // Spring Boot students
        if (question.contains("spring")) {

            long count = students.stream()
                    .filter(s -> s.getCourse().equalsIgnoreCase("Spring Boot"))
                    .count();

            return "There are " + count + " Spring Boot students.";

        }

        // List students
        if (question.contains("list")) {

            StringBuilder builder = new StringBuilder();

            for (Student s : students) {

                builder.append(s.getName())
                        .append(" (")
                        .append(s.getCourse())
                        .append(")<br>");

            }

            return builder.toString();

        }

        return """
                Sorry 😅

                I don't understand that yet.

                Try asking:

                • Total students

                • Java students

                • Spring Boot students

                • List students
                """;
    }
}