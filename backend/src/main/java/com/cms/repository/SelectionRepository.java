package com.cms.repository;

import com.cms.entity.Course;
import com.cms.entity.Selection;
import com.cms.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface SelectionRepository extends JpaRepository<Selection, Long> {
    List<Selection> findByStudent(User student);
    boolean existsByStudentAndCourse(User student, Course course);
    
    @Query("SELECT s.course FROM Selection s WHERE s.student = ?1")
    List<Course> findCoursesByStudent(User student);
}
