package com.example.academy.auth.client;

import com.example.academy.auth.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(name = "course-service", configuration = FeignConfig.class)
public interface CourseServiceClient {

    @GetMapping("/api/courses/{id}")
    Map<String, Object> getCourseById(@PathVariable("id") Long id);

    @GetMapping("/api/courses/user/{userId}")
    Map<String, Object> getCoursesByUser(@PathVariable("userId") Long userId);

    @PostMapping("/api/courses/{courseId}/enroll")
    Map<String, Object> enrollUser(@PathVariable("courseId") Long courseId, @RequestBody Map<String, Object> enrollmentRequest);

    @PostMapping("/api/courses/{courseId}/unenroll")
    void unenrollUser(@PathVariable("courseId") Long courseId, @RequestBody Map<String, Object> unenrollmentRequest);
}
