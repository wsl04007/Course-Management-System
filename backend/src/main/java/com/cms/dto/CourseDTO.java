package com.cms.dto;

import lombok.Data;
import javax.validation.constraints.*;

@Data
public class CourseDTO {
    private Long id;
    
    @NotBlank(message = "课程编号不能为空")
    private String courseCode;
    
    @NotBlank(message = "课程名称不能为空")
    private String name;
    
    private String description;
    private String teacher;
    
    @Min(value = 1, message = "学分至少为1")
    @Max(value = 10, message = "学分不能超过10")
    private Integer credits;
    
    @Min(value = 10, message = "容量至少为10")
    @Max(value = 200, message = "容量不能超过200")
    private Integer capacity;
    
    private String schedule;
    private String location;
    private String semester;
}
