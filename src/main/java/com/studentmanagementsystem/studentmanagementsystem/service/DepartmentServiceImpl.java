package com.studentmanagementsystem.studentmanagementsystem.service;

import com.lowagie.text.*;
import com.studentmanagementsystem.studentmanagementsystem.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;



}
