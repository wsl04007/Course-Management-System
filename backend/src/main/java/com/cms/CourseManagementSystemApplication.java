package com.cms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CourseManagementSystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(CourseManagementSystemApplication.class, args);
        System.out.println("========================================");
        System.out.println("课程管理系统启动成功！");
        System.out.println("访问地址: http://localhost:8080");
        System.out.println("========================================");
    }
}
