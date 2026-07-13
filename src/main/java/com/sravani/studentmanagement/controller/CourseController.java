package com.sravani.studentmanagement.controller;

import com.sravani.studentmanagement.entity.Course;
import com.sravani.studentmanagement.service.CourseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    public List<Course> getAllCourses() {
        return courseService.getAllCourses();
    }

    @PostMapping
    public ResponseEntity<?> addCourse(@RequestBody Course course) {

        if (courseService.findByCourseName(course.getCourseName()).isPresent()) {
            return ResponseEntity.badRequest()
                    .body("Course already exists");
        }

        return ResponseEntity.ok(
                courseService.saveCourse(course)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCourse(
            @PathVariable Long id) {

        courseService.deleteCourse(id);

        return ResponseEntity.ok("Course Deleted Successfully");
    }
}