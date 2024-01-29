package com.studentmanagementsystem.studentmanagementsystem.repository;


import com.studentmanagementsystem.studentmanagementsystem.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Long> {

    Department findByDepartmentName(String departmentName);
}
