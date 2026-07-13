package com.sravani.studentmanagement.service;

import com.sravani.studentmanagement.entity.Course;
import com.sravani.studentmanagement.repository.CourseRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseService {

    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Course saveCourse(Course course) {
        return courseRepository.save(course);
    }

    public Optional<Course> findByCourseName(String name) {
        return courseRepository.findByCourseName(name);
    }

    public void deleteCourse(Long id) {
        courseRepository.deleteById(id);
    }

}