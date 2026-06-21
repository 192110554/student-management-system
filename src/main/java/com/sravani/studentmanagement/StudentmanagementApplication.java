package com.sravani.studentmanagement;

import com.sravani.studentmanagement.entity.Student;
import com.sravani.studentmanagement.repository.StudentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class StudentmanagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(StudentmanagementApplication.class, args);
	}

	@Bean
	CommandLineRunner runner(StudentRepository studentRepository) {
		return args -> {

			Student student = new Student();

			student.setName("Sravani");
			student.setEmail("sravani@gmail.com");
			student.setCourse("Java");

			studentRepository.save(student);

			System.out.println("Student Saved Successfully");
		};
	}
}