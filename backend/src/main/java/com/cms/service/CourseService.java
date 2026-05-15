package com.cms.service;

import com.cms.dto.CourseDTO;
import com.cms.entity.Course;
import com.cms.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CourseService {
    @Autowired
    private CourseRepository courseRepository;
    
    public List<Course> searchCourses(String keyword, String teacher, Integer credits, String semester) {
        if (StringUtils.hasText(keyword)) {
            return courseRepository.findByNameContainingOrCourseCodeContaining(keyword, keyword);
        }
        if (StringUtils.hasText(teacher)) {
            return courseRepository.findByTeacherContaining(teacher);
        }
        if (credits != null) {
            return courseRepository.findByCredits(credits);
        }
        if (StringUtils.hasText(semester)) {
            return courseRepository.findBySemester(semester);
        }
        return courseRepository.findAll();
    }
    
    public Course getCourseById(Long id) {
        return courseRepository.findById(id).orElse(null);
    }
    
    public Course createCourse(CourseDTO dto) {
        Course course = new Course();
        course.setCourseCode(dto.getCourseCode());
        course.setName(dto.getName());
        course.setDescription(dto.getDescription());
        course.setTeacher(dto.getTeacher());
        course.setCredits(dto.getCredits());
        course.setCapacity(dto.getCapacity());
        course.setEnrolled(0);
        course.setSchedule(dto.getSchedule());
        course.setLocation(dto.getLocation());
        course.setSemester(dto.getSemester());
        course.setStatus(0);
        course.setCreateTime(LocalDateTime.now());
        course.setUpdateTime(LocalDateTime.now());
        return courseRepository.save(course);
    }
    
    public Course updateCourse(Long id, CourseDTO dto) throws Exception {
        Course course = getCourseById(id);
        if (course == null) throw new Exception("课程不存在");
        course.setCourseCode(dto.getCourseCode());
        course.setName(dto.getName());
        course.setDescription(dto.getDescription());
        course.setTeacher(dto.getTeacher());
        course.setCredits(dto.getCredits());
        course.setCapacity(dto.getCapacity());
        course.setSchedule(dto.getSchedule());
        course.setLocation(dto.getLocation());
        course.setSemester(dto.getSemester());
        course.setUpdateTime(LocalDateTime.now());
        return courseRepository.save(course);
    }
    
    public void deleteCourse(Long id) throws Exception {
        Course course = getCourseById(id);
        if (course == null) throw new Exception("课程不存在");
        courseRepository.delete(course);
    }
    
    public Course publishCourse(Long id) {
        Course course = getCourseById(id);
        course.setStatus(1);
        course.setUpdateTime(LocalDateTime.now());
        return courseRepository.save(course);
    }
}
