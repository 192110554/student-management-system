package com.sravani.studentmanagement.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendWelcomeEmail(String toEmail,
                                 String studentName,
                                 String course) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(toEmail);

        message.setSubject("Welcome to Student Management System");

        message.setText(
                "Hello " + studentName + ",\n\n" +

                        "Welcome to Student Management System.\n\n" +

                        "Your registration has been completed successfully.\n\n" +

                        "Course : " + course + "\n\n" +

                        "Regards,\nStudent Management System"
        );

        mailSender.send(message);
    }

    public void sendAttendanceEmail(String toEmail,
                                    String studentName,
                                    String status) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(toEmail);

        message.setSubject("Attendance Update");

        message.setText(
                "Hello " + studentName + ",\n\n" +

                        "Your attendance has been marked.\n\n" +

                        "Status : " + status + "\n\n" +

                        "Regards,\nStudent Management System"
        );

        mailSender.send(message);
    }
}