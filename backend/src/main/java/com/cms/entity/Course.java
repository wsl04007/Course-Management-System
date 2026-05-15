package com.cms.entity;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "courses")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String courseCode;
    
    @Column(nullable = false)
    private String name;
    
    private String description;
    private String teacher;
    private Integer credits;
    private Integer capacity;
    private Integer enrolled;
    private String schedule;
    private String location;
    private String semester;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
