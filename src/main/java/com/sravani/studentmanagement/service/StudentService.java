package com.sravani.studentmanagement.service;

import com.sravani.studentmanagement.entity.Student;
import com.sravani.studentmanagement.repository.StudentRepository;
import com.sravani.studentmanagement.exception.StudentNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }
    public Page<Student> getStudents(Pageable pageable) {
        return studentRepository.findAll(pageable);
    }
    public Student getStudentById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() ->
                        new StudentNotFoundException("Student not found with id " + id));
    }
    public List<Student> getStudentByEmail(String email) {
        return studentRepository.findByEmail(email);
    }

    public List<Student> getStudentsByName(String name) {
        return studentRepository.findByName(name);
    }
    public List<Student> getStudentsByCourse(String course) {
        return studentRepository.findByCourse(course);
    }
    public Student saveStudent(Student student) {
        return studentRepository.save(student);
    }
    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }
    public Student updateStudent(Long id, Student updatedStudent) {

        Student student = studentRepository.findById(id).orElse(null);

        if (student != null) {
            student.setName(updatedStudent.getName());
            student.setEmail(updatedStudent.getEmail());
            student.setCourse(updatedStudent.getCourse());

            return studentRepository.save(student);
        }

        return null;
    }
}