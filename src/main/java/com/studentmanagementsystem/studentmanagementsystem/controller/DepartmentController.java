package com.studentmanagementsystem.studentmanagementsystem.controller;

import com.studentmanagementsystem.studentmanagementsystem.entity.Department;
import com.studentmanagementsystem.studentmanagementsystem.exception.ForeignKeyViolationException;
import com.studentmanagementsystem.studentmanagementsystem.repository.DepartmentRepository;
import com.studentmanagementsystem.studentmanagementsystem.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;
import java.util.Optional;

@Controller
public class DepartmentController {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private DepartmentService departmentService;

    @RequestMapping(value = "/departments", method = RequestMethod.POST)
    public String saveDepartment(@ModelAttribute("department") Department department, BindingResult result) {
        if (result.hasErrors()) {
            // Log or inspect validation errors
            System.out.println("Validation errors: " + result.getAllErrors());
            return "create_department";
        }
        departmentRepository.save(department);
        return "redirect:/departments";
    }


    @GetMapping("/department/new")
    public String showAddDepartmentForm(Model model) {
        Department department = new Department();
        model.addAttribute("department", department);
        return "create_department";
    }

    @RequestMapping(value = "/departments", method = RequestMethod.GET)
    public String DepartmentList(Model model) {
        model.addAttribute("departments", departmentRepository.findAll());
        return "departments";
    }

    @GetMapping("departments/edit/{id}")
    public String showEditStudentForm(@PathVariable Long id, Model model) {
        Optional<Department> department = departmentRepository.findById(id);
        model.addAttribute("department", department.orElse(null));
        return "edit_department";
    }

    @PostMapping("/departments/{id}")
    public String updateStudent(@PathVariable Long id, @Valid @ModelAttribute("department") Department updatedDepartment, BindingResult result) {
        Optional<Department> existingDepartment = departmentRepository.findById(id);

        if (result.hasErrors()) {
            // If there are validation errors, return to the form page
            return "edit_department";
        }
        if (existingDepartment.isPresent()) {
            Department department = existingDepartment.get();
            department.setDepartmentId(updatedDepartment.getDepartmentId());
            department.setDepartmentName(updatedDepartment.getDepartmentName());

            departmentRepository.save(department);
        }

        return "redirect:/departments";


    }

    //delete student

    @GetMapping("/departments/{id}")
    public String deleteDepartment(@PathVariable Long id) {
        try {
            departmentRepository.deleteById(id);
        }catch (Exception e){
            e.printStackTrace();
        }
        return "redirect:/departments";
    }


}



