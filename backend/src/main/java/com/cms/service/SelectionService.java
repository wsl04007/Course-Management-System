package com.cms.service;

import com.cms.entity.Course;
import com.cms.entity.Selection;
import com.cms.entity.User;
import com.cms.repository.CourseRepository;
import com.cms.repository.SelectionRepository;
import com.cms.repository.UserRepository;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class SelectionService {
    @Autowired
    private SelectionRepository selectionRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private UserRepository userRepository;
    
    @Transactional
    public Selection selectCourse(String username, Long courseId) throws Exception {
        User student = userRepository.findByUsername(username)
                .orElseThrow(() -> new Exception("学生不存在"));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new Exception("课程不存在"));
        
        if (course.getStatus() != 1) throw new Exception("课程未发布");
        if (course.getEnrolled() >= course.getCapacity()) throw new Exception("课程容量已满");
        if (selectionRepository.existsByStudentAndCourse(student, course)) throw new Exception("已选过这门课");
        
        List<Selection> existing = selectionRepository.findByStudent(student);
        for (Selection s : existing) {
            if (s.getCourse().getSchedule().equals(course.getSchedule())) {
                throw new Exception("时间冲突：" + s.getCourse().getName());
            }
        }
        
        int totalCredits = existing.stream().mapToInt(s -> s.getCourse().getCredits()).sum();
        if (totalCredits + course.getCredits() > 25) {
            throw new Exception("学分超过上限（最多25学分）");
        }
        
        Selection selection = new Selection();
        selection.setStudent(student);
        selection.setCourse(course);
        selection.setSelectTime(LocalDateTime.now());
        selection.setStatus(0);
        
        course.setEnrolled(course.getEnrolled() + 1);
        courseRepository.save(course);
        return selectionRepository.save(selection);
    }
    
    public List<Course> getStudentCourses(String username) {
        User student = userRepository.findByUsername(username).orElse(null);
        if (student == null) return List.of();
        return selectionRepository.findCoursesByStudent(student);
    }
    
    public byte[] exportTimetableToPdf(String username) throws Exception {
        List<Course> courses = getStudentCourses(username);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, out);
        document.open();
        
        Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
        Paragraph title = new Paragraph("我的课表", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        document.add(new Paragraph(" "));
        
        User student = userRepository.findByUsername(username).orElse(null);
        document.add(new Paragraph("学生：" + (student != null ? student.getName() : "")));
        document.add(new Paragraph("导出时间：" + LocalDateTime.now()));
        document.add(new Paragraph(" "));
        
        PdfPTable table = new PdfPTable(6);
        table.setWidthPercentage(100);
        table.addCell("课程编号");
        table.addCell("课程名称");
        table.addCell("教师");
        table.addCell("学分");
        table.addCell("时间");
        table.addCell("地点");
        
        for (Course c : courses) {
            table.addCell(c.getCourseCode());
            table.addCell(c.getName());
            table.addCell(c.getTeacher());
            table.addCell(c.getCredits().toString());
            table.addCell(c.getSchedule());
            table.addCell(c.getLocation());
        }
        document.add(table);
        document.close();
        return out.toByteArray();
    }
    
    @Transactional
    public void dropCourse(Long selectionId) throws Exception {
        Selection selection = selectionRepository.findById(selectionId)
                .orElseThrow(() -> new Exception("选课记录不存在"));
        Course course = selection.getCourse();
        course.setEnrolled(course.getEnrolled() - 1);
        courseRepository.save(course);
        selectionRepository.delete(selection);
    }
}
