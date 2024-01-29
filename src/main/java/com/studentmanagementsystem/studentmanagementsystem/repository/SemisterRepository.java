package com.studentmanagementsystem.studentmanagementsystem.repository;

import com.studentmanagementsystem.studentmanagementsystem.entity.Semester;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SemisterRepository extends JpaRepository<Semester,Long> {


    List<Semester> findAllByDepartmentDepartmentId(Long departmentId);


}
