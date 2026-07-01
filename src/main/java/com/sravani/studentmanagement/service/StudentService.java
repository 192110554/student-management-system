package com.sravani.studentmanagement.service;

import com.sravani.studentmanagement.entity.Student;
import com.sravani.studentmanagement.repository.StudentRepository;
import com.sravani.studentmanagement.exception.StudentNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import jakarta.servlet.http.HttpServletResponse;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

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
        return studentRepository.findByEmailContainingIgnoreCase(email);
    }

    public List<Student> getStudentsByName(String name) {
        return studentRepository.findByNameContainingIgnoreCase(name);
    }
    public List<Student> getStudentsByCourse(String course) {
        return studentRepository.findByCourseContainingIgnoreCase(course);
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

    public byte[] exportStudentsToExcel() throws IOException {

        List<Student> students = studentRepository.findAll();

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Students");

        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("ID");
        header.createCell(1).setCellValue("Name");
        header.createCell(2).setCellValue("Email");
        header.createCell(3).setCellValue("Course");

        int rowNum = 1;

        for (Student student : students) {

            Row row = sheet.createRow(rowNum++);

            row.createCell(0).setCellValue(student.getId());
            row.createCell(1).setCellValue(student.getName());
            row.createCell(2).setCellValue(student.getEmail());
            row.createCell(3).setCellValue(student.getCourse());
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        workbook.write(outputStream);
        workbook.close();

        return outputStream.toByteArray();
    }

    public void exportToPdf(HttpServletResponse response) throws IOException {

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition",
                "attachment; filename=students.pdf");

        Document document = new Document();

        try {

            PdfWriter.getInstance(document, response.getOutputStream());

            document.open();

            document.add(new Paragraph("Student Management System"));
            document.add(new Paragraph(" "));
            document.add(new Paragraph("Student Report"));
            document.add(new Paragraph(" "));

            PdfPTable table = new PdfPTable(4);

            table.addCell("ID");
            table.addCell("Name");
            table.addCell("Email");
            table.addCell("Course");

            List<Student> students = studentRepository.findAll();

            for (Student student : students) {

                table.addCell(String.valueOf(student.getId()));
                table.addCell(student.getName());
                table.addCell(student.getEmail());
                table.addCell(student.getCourse());
            }

            document.add(table);

        } catch (Exception e) {

            e.printStackTrace();

        } finally {

            document.close();
        }
    }
}