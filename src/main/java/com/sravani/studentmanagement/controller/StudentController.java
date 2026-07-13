package com.sravani.studentmanagement.controller;

import com.sravani.studentmanagement.service.EmailService;
import com.sravani.studentmanagement.entity.Student;
import com.sravani.studentmanagement.service.StudentService;
import com.sravani.studentmanagement.dto.StudentDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

@RestController
@RequestMapping("/api")
public class StudentController {

    private final StudentService studentService;

    private final EmailService emailService;

    public StudentController(StudentService studentService,
                             EmailService emailService) {

        this.studentService = studentService;
        this.emailService = emailService;
    }

    @GetMapping("/students")
    public Page<Student> getStudents(Pageable pageable) {
        return studentService.getStudents(pageable);
    }

    @PostMapping("/students")
    public ResponseEntity<StudentDTO> saveStudent(@Valid @RequestBody StudentDTO studentDTO) {

        Student student = new Student();

        student.setName(studentDTO.getName());
        student.setEmail(studentDTO.getEmail());
        student.setCourse(studentDTO.getCourse());
        student.setImageUrl(studentDTO.getImageUrl());

        Student savedStudent = studentService.saveStudent(student);

        emailService.sendWelcomeEmail(
                savedStudent.getEmail(),
                savedStudent.getName(),
                savedStudent.getCourse()
        );
        StudentDTO responseDTO = new StudentDTO();

        responseDTO.setId(savedStudent.getId());
        responseDTO.setName(savedStudent.getName());
        responseDTO.setEmail(savedStudent.getEmail());
        responseDTO.setCourse(savedStudent.getCourse());
        responseDTO.setImageUrl(savedStudent.getImageUrl());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(responseDTO);
    }

    @GetMapping("/students/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable Long id) {

        Student student = studentService.getStudentById(id);

        return ResponseEntity.ok(student);
    }
    @GetMapping("/students/email/{email}")
    public List<Student> getStudentByEmail(
            @PathVariable String email) {

        return studentService.getStudentByEmail(email);
    }

    @GetMapping("/students/name/{name}")
    public ResponseEntity<List<Student>> getStudentsByName(@PathVariable String name) {

        List<Student> students = studentService.getStudentsByName(name);

        return ResponseEntity.ok(students);
    }
    @GetMapping("/students/course/{course}")
    public List<Student> getStudentsByCourse(@PathVariable String course) {
        return studentService.getStudentsByCourse(course);
    }
    @DeleteMapping("/students/{id}")
    public String deleteStudent(@PathVariable Long id) {

        studentService.deleteStudent(id);

        return "Student Deleted Successfully";
    }
    @PutMapping("/students/{id}")
    public Student updateStudent(@PathVariable Long id,
                                 @RequestBody Student updatedStudent) {

        return studentService.updateStudent(id, updatedStudent);
    }
    @GetMapping("/students/export")
    public ResponseEntity<byte[]> exportStudents() throws Exception {

        byte[] excelFile = studentService.exportStudentsToExcel();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=students.xlsx")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(excelFile);
    }
    @GetMapping("/students/export/pdf")
    public void exportToPdf(HttpServletResponse response) throws Exception {

        studentService.exportToPdf(response);

    }
}