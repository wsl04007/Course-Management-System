package com.cms.repository;

import com.cms.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByNameContainingOrCourseCodeContaining(String name, String courseCode);
    List<Course> findByTeacherContaining(String teacher);
    List<Course> findByCredits(Integer credits);
    List<Course> findBySemester(String semester);
}
