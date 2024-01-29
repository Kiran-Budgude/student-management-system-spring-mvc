package com.studentmanagementsystem.studentmanagementsystem.service;


import com.lowagie.text.*;
import com.lowagie.text.pdf.CMYKColor;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.studentmanagementsystem.studentmanagementsystem.entity.Student;
import com.studentmanagementsystem.studentmanagementsystem.repository.StudentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
public class StudentServiceImpl implements StudentService {


    private StudentRepository studentRepository;

    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public void saveStudent(Student student) {
        studentRepository.save(student);
    }

    @Override
    public List<Student> getAllStudents() {
        List<Student> students = studentRepository.findAll();
        return students;
    }

    @Override
    public Optional<Student> getStudentById(Long id) {
        Optional<Student> student = studentRepository.findById(id);
        return student;
    }

    @Override
    public void deleteStudent(Long studentId) {
        studentRepository.deleteById(studentId);
    }

    @Override
    public List<Student> searchStudents(String searchInput) {
        return studentRepository.findByFirstNameContainingOrLastNameContainingOrDepartmentContaining(
                searchInput, searchInput, searchInput);
    }

    @Override
    public List<Student> getStudentReportPdf(HttpServletResponse response) throws IOException {

        List<Student> studentList = studentRepository.findAll();

        if (!studentList.isEmpty()) {

            try {
                Document document = new Document(PageSize.A3);

                PdfWriter.getInstance(document, response.getOutputStream());
                document.open();

                com.lowagie.text.Font font = FontFactory.getFont(FontFactory.TIMES_ROMAN);
                font.setSize(20);

                Paragraph paragraph = new Paragraph("Student Report", font);
                paragraph.setAlignment(Paragraph.ALIGN_CENTER);

                document.add(paragraph);

                PdfPTable pdfPTable = new PdfPTable(11);
                pdfPTable.setWidths(new int[]{9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9});
                pdfPTable.setWidthPercentage(100f);
                pdfPTable.setSpacingBefore(5);

                PdfPCell pdfPCell = new PdfPCell();
                pdfPCell.setBackgroundColor(CMYKColor.BLUE);
                pdfPCell.setHorizontalAlignment(120);
                pdfPCell.setPadding(7);

                com.lowagie.text.Font font1 = FontFactory.getFont(FontFactory.TIMES_ROMAN);
                font1.setColor(CMYKColor.RED);

                pdfPCell.setPhrase(new Phrase("Student_Id", font1));
                pdfPTable.addCell(pdfPCell);

                pdfPCell.setPhrase(new Phrase("First_Name", font1));
                pdfPTable.addCell(pdfPCell);

                pdfPCell.setPhrase(new Phrase("Middle_Name", font1));
                pdfPTable.addCell(pdfPCell);

                pdfPCell.setPhrase(new Phrase("Surname", font1));
                pdfPTable.addCell(pdfPCell);

                pdfPCell.setPhrase(new Phrase("DOB", font1));
                pdfPTable.addCell(pdfPCell);

                pdfPCell.setPhrase(new Phrase("Contact", font1));
                pdfPTable.addCell(pdfPCell);

                pdfPCell.setPhrase(new Phrase("Email", font1));
                pdfPTable.addCell(pdfPCell);

                pdfPCell.setPhrase(new Phrase("Address", font1));
                pdfPTable.addCell(pdfPCell);

                pdfPCell.setPhrase(new Phrase("Department", font1));
                pdfPTable.addCell(pdfPCell);

                pdfPCell.setPhrase(new Phrase("Collage_Year", font1));
                pdfPTable.addCell(pdfPCell);

                pdfPCell.setPhrase(new Phrase("Semester", font1));
                pdfPTable.addCell(pdfPCell);


                for (Student studentResponse : studentList) {
                    pdfPTable.addCell((studentResponse.getId() != null ? studentResponse.getId().toString() : ""));
                    pdfPTable.addCell(studentResponse.getFirstName() != null ? studentResponse.getFirstName() : "");
                    pdfPTable.addCell(studentResponse.getMiddleName() != null ? studentResponse.getMiddleName() : "");
                    pdfPTable.addCell(studentResponse.getLastName() != null ? studentResponse.getLastName() : "");
                    pdfPTable.addCell(studentResponse.getDateOfBirth() != null ? studentResponse.getDateOfBirth() : "");
                    pdfPTable.addCell(studentResponse.getPhoneNumber() != null ? studentResponse.getPhoneNumber() : "");
                    pdfPTable.addCell(studentResponse.getEmail() != null ? studentResponse.getEmail() : "");
                    pdfPTable.addCell(studentResponse.getAddress() != null ? studentResponse.getAddress() : "");
                    pdfPTable.addCell(studentResponse.getDepartment() != null ? studentResponse.getDepartment() : "");
                    pdfPTable.addCell(studentResponse.getCollageYear() != null ? studentResponse.getCollageYear() : "");
                    pdfPTable.addCell(studentResponse.getSemesters() != null ? studentResponse.getSemesters().toString() : "");


                }
                document.add(pdfPTable);
                document.close();

                return studentList;


            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
        } else {
            throw new RemoteException("List not found");

        }

    }

    @Override
    public int generateRandomDigit(int minDigits, int maxDigits) {

        Random random = new Random();
        return random.nextInt(maxDigits - minDigits + 1) + minDigits;
    }

          @Override
          public String saveImage(MultipartFile image) throws IOException {
           // Save the image to a directory or external storage, and return the image name/path
           // Example: Store the image in a directory within your project
           String imageName = UUID.randomUUID().toString() + "_" + image.getOriginalFilename();
           Files.write(Paths.get("Gallery", imageName), image.getBytes());
           return imageName;
       }

    @Override
    public Page<Student> findPaginatedStudent(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize); // Adjust pageNo to be zero-based
        return studentRepository.findAll(pageable);
    }


}
