package com.studentmanagementsystem.studentmanagementsystem.controller;

import com.studentmanagementsystem.studentmanagementsystem.config.FileUploadUtil;
import com.studentmanagementsystem.studentmanagementsystem.entity.Department;
import com.studentmanagementsystem.studentmanagementsystem.entity.Student;
import com.studentmanagementsystem.studentmanagementsystem.repository.DepartmentRepository;
import com.studentmanagementsystem.studentmanagementsystem.repository.StudentRepository;
import com.studentmanagementsystem.studentmanagementsystem.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.ZipOutputStream;

@Controller
public class StudentController {

    private StudentService studentService;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private StudentRepository studentRepository;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }


    //handler method to handle list students andreturn modeand view


    //save students
    @RequestMapping(value = "/students", method = RequestMethod.POST)
    public String addStudent(@Valid @ModelAttribute("student") Student student, @RequestParam("file") MultipartFile file, BindingResult result) throws IOException {
        if (result.hasErrors()) {
            // Log or inspect validation errors
            System.out.println("Validation errors: " + result.getAllErrors());
            return "create_student";
        }
        if (!file.isEmpty()) {
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
            if (fileName.contains("..")) {
                System.out.println("Not A Valid File");
            }

            String uploadDir = "E:/student-management/system/student-management-system/src/main/resources/student-photos/";

            FileUploadUtil.saveFile(uploadDir, fileName, file);
            try {
                student.setPhotos(Base64.getEncoder().encodeToString(file.getBytes()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
            Department department = departmentRepository.findByDepartmentName(student.getDepartment());
            if (department != null) {
                student.setDepartments(department);
            } else {
                Department department1 = new Department();
                int randomNumber = studentService.generateRandomDigit(3, 5);
                long departmentId = System.currentTimeMillis() % (long) Math.pow(10, randomNumber);
                department1.setDepartmentId(departmentId);
                department1.setDepartmentName(student.getDepartment());
                student.setDepartments(department1);
                departmentRepository.save(department1);
                studentService.saveStudent(student);
            }
            studentService.saveStudent(student);
            return "redirect:/students";
        }


    @GetMapping("/students/new")
    public String showAddStudentForm(Model model) {
        Student student = new Student();
        model.addAttribute("student", student);
        model.addAttribute("departments", departmentRepository.findAll());

        return "create_student";
    }


    //list of all student
    @RequestMapping(value = "/students", method = RequestMethod.GET)
    public String studentsList(Model model) {
        /*model.addAttribute("students", studentService.getAllStudents());
        return "students";*/
        return findPaginatedStudent(1,model);
    }

    //update student from page
    @GetMapping("students/edit/{id}")
    public String showEditStudentForm(@PathVariable Long id, Model model) {
        Optional<Student> student = studentService.getStudentById(id);
        model.addAttribute("student", student.orElse(null));
        return "edit_student";
    }

    //update student actual
    @PostMapping("/students/{id}")
    public String updateStudent(@PathVariable Long id, @Valid @ModelAttribute("student") Student updatedStudent,@RequestParam("file") MultipartFile file, BindingResult result) {
        Optional<Student> existingStudent = studentService.getStudentById(id);

        if (result.hasErrors()) {
            // If there are validation errors, return to the form page
            return "edit_student";
        }
        if (existingStudent.isPresent()) {
            Student student = existingStudent.get();
            student.setFirstName(updatedStudent.getFirstName());
            student.setMiddleName(updatedStudent.getMiddleName());
            student.setLastName(updatedStudent.getLastName());
            student.setEmail(updatedStudent.getEmail());
            student.setDepartment(updatedStudent.getDepartment());
            student.setAddress(updatedStudent.getAddress());
            student.setPhoneNumber(updatedStudent.getPhoneNumber());
            student.setDateOfBirth(updatedStudent.getDateOfBirth());
            Department department = departmentRepository.findByDepartmentName(student.getDepartment());
            try {
            if(department != null){
                    student.setDepartments(department);
                }else {
                    Department department1 = new Department();
                    int randomNumber = studentService.generateRandomDigit(3,5);
                    long departmentId = System.currentTimeMillis() % (long) Math.pow(10, randomNumber);
                    department1.setDepartmentId(departmentId);
                    department1.setDepartmentName(updatedStudent.getDepartment());
                    departmentRepository.save(department1);
                    student.setDepartments(department1);
                    studentService.saveStudent(student);


                }
                if (!file.isEmpty()) {
                    String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
                    if (fileName.contains("..")) {
                        System.out.println("Not A Valid File");
                    }

                    String uploadDir = "E:/student-management/system/student-management-system/src/main/resources/student-photos/";

                    FileUploadUtil.saveFile(uploadDir, fileName, file);
                    try {
                        student.setPhotos(Base64.getEncoder().encodeToString(file.getBytes()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                studentService.saveStudent(student);

            } catch (Exception e) {
                e.printStackTrace();
            }


        }

        return "redirect:/students";


    }

    //delete student

    @GetMapping("/students/{id}")
    public String deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return "redirect:/students";
    }

    @GetMapping("/csv")
    public void exportStudentsCsv(HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=students.csv");

        List<Student> students = studentService.getAllStudents();

        try (PrintWriter writer = response.getWriter()) {
            // Write CSV header
            writer.println("ID,First Name,Middle Name,SurName,Contact,Email,Department,DOB,Address");

            // Write student records
            for (Student student : students) {
                writer.println(
                        "\"" + (student.getId() != null ? student.getId() : "") + "\"," +
                                "\"" + (student.getFirstName() != null ? student.getFirstName() : "") + "\"," +
                                "\"" + (student.getMiddleName() != null ? student.getMiddleName() : "") + "\"," +
                                "\"" + (student.getLastName() != null ? student.getLastName() : "") + "\"," +
                                "\"" + (student.getPhoneNumber() != null ? student.getPhoneNumber() : "") + "\"," +
                                "\"" + (student.getEmail() != null ? student.getEmail() : "") + "\"," +
                                "\"" + (student.getDepartment() != null ? student.getDepartment() : "") + "\"," +
                                "\"" + (student.getDateOfBirth() != null ? student.getDateOfBirth() : "") + "\"," +
                                "\"" + (student.getAddress() != null ? student.getAddress() : "") + "\""
                );
            }

        }
    }

    @GetMapping("/students/search")
    public String searchStudents(
            @RequestParam(name = "searchInput") String searchInput,
            Model model
    ) {
        List<Student> students = studentService.searchStudents(searchInput);
        model.addAttribute("students", students);
        return "students";
    }

    @GetMapping(value = "/pdf", produces = "application/pdf")
    public ResponseEntity<?> exportStudentsPdf(HttpServletResponse response) throws IOException {

        String fileSufix = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".pdf";
        String fileName = "StudentReport" + fileSufix;
        OutputStream outputStream;
        outputStream = response.getOutputStream();
        ZipOutputStream outputStream1 = new ZipOutputStream(outputStream);
        outputStream1.setComment("");

        response.setContentType("application/pdf");
        response.addHeader("Content-Disposition", "attachment; filename=" + fileName);

        return ResponseEntity.ok().body(studentService.getStudentReportPdf(response));

    }

    // Existing methods

    private List<Student> paginateItems(List<Student> studentList, int page, int size) {
        int start = page * size;
        int end = Math.min((page + 1) * size, studentList.size());

        return studentList.subList(start, end);
    }

    private int calculateTotalPages(List<Student> studentList, int size) {
        return (int) Math.ceil((double) studentList.size() / size);
    }

    @GetMapping("/page/{pageNo}")
    public String findPaginatedStudent(@PathVariable(value = "pageNo") int pageNo, Model model) {
        int pageSize = 5; // Set your desired page size

        Page<Student> pagList = studentService.findPaginatedStudent(pageNo, pageSize);
        List<Student> students = pagList.getContent();

        model.addAttribute("students", students);
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", pagList.getTotalPages());
        model.addAttribute("totalItems", pagList.getTotalElements());

        return "students";
    }



}
