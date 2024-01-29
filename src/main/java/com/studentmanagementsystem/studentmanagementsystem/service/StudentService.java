package com.studentmanagementsystem.studentmanagementsystem.service;


import com.studentmanagementsystem.studentmanagementsystem.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface StudentService {

    void saveStudent(Student student);

    List<Student> getAllStudents();

    Optional<Student> getStudentById(Long id);

    void deleteStudent(Long studentId);

    List<Student> searchStudents(String searchInput);

    List<Student> getStudentReportPdf(HttpServletResponse response) throws IOException;

     int generateRandomDigit(int minDigits, int maxDigits);

    String saveImage(MultipartFile image) throws IOException;

    Page<Student> findPaginatedStudent(int pageNo, int pageSize);

}
