package com.studentmanagementsystem.studentmanagementsystem.controller;

import com.studentmanagementsystem.studentmanagementsystem.entity.Department;
import com.studentmanagementsystem.studentmanagementsystem.entity.Semester;
import com.studentmanagementsystem.studentmanagementsystem.repository.DepartmentRepository;
import com.studentmanagementsystem.studentmanagementsystem.repository.SemisterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Controller
public class SemisterController {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private SemisterRepository semisterRepository;


    @RequestMapping(value = "/semesters", method = RequestMethod.GET)
    public String semesterList(Model model) {
        List<Department> departments = departmentRepository.findAll();
        List<Semester> allSemesters = new ArrayList<>();

        for (Department department : departments) {
            List<Semester> semesters = semisterRepository.findAllByDepartmentDepartmentId(department.getDepartmentId());
            allSemesters.addAll(semesters);
        }

        model.addAttribute("semesters", allSemesters);
        return "semesters";
    }


    @GetMapping("/semesters/{id}")
    public String deleteSemester(@PathVariable Long id) {
        try {
            semisterRepository.deleteById(id);
        }catch (Exception e){
            e.printStackTrace();
        }
        return "redirect:/semesters";
    }

    @PostMapping("/semesters/{semesterId}")
    public String updateSemester(@Valid @ModelAttribute("semester") Semester semester, BindingResult result, Model model, @RequestParam("semesterId") Long semesterId) {


        Optional<Semester> optionalSemester = semisterRepository.findById(semesterId);

        if (result.hasErrors()) {
            model.addAttribute("semester", optionalSemester.get());
            // If there are validation errors, return to the form page
            return "edit_semester";
        }
        if (optionalSemester.isPresent()) {
            Semester semester1 = optionalSemester.get();
            semester1.setSemesterNumber(semester.getSemesterNumber());
            semester1.setDepartment(semester.getDepartment());
            semester1.setSubjects(semester.getSubjects());

            semisterRepository.save(semester1);


        }

        return "redirect:/semesters";


    }

    @GetMapping("semesters/edit/{id}")
    public String showEditStudentForm(@PathVariable Long id, Model model) {
        Optional<Semester> semester = semisterRepository.findById(id);
        model.addAttribute("semester", semester.orElse(null));
        return "edit_semester";
    }

    @RequestMapping(value = "/semesters", method = RequestMethod.POST)
    public String saveSemester(@ModelAttribute("semester") Semester semester, BindingResult result) {
        if (result.hasErrors()) {
            // Log or inspect validation errors
            System.out.println("Validation errors: " + result.getAllErrors());
            return "create_semester";
        }
        semisterRepository.save(semester);
        return "redirect:/semesters";
    }


    @GetMapping("/semester/new")
    public String showAddDepartmentForm(Model model) {
        Semester semester = new Semester();
        model.addAttribute("semester", semester);
        return "create_semester";
    }

}
