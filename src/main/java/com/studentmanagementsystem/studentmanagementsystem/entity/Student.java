package com.studentmanagementsystem.studentmanagementsystem.entity;


import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "students")
@Data
public class Student {

    @Id
    private Long id;

    @Column(name = "first_name")
    @NotEmpty(message = "First name cannot be empty")
    private String firstName;


    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "surname")
    @NotEmpty(message = "Last name cannot be empty")
    private String lastName;

    @Column(name = "email_id")
    @Email(message = "Please enter a valid email address")
    private String email;


    @Column(name = "department")
    private String department;

    @Column(name = "address")
    private String address;

    @Column(name = "phone_number")
    @Size(min = 10, max = 15, message = "Phone number must be between 10 and 15 characters")
    private String phoneNumber;

    @Column(name = "date_of_birth")
    private String dateOfBirth;

    @Column(name = "collage_year")
    private String collageYear;

    @Column(nullable = true, length = 64)
    private String photos;


    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department departments;

    @ManyToOne
    @JoinColumn(name = "year_id")
    private Year year;

    @ManyToMany
    @JoinTable(
            name = "student_semester",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "semester_id")
    )
    private Set<Semester> semesters = new HashSet<>();

    public Student() {
    }

    public Student(Long id, String firstName, String middleName, String lastName, String email, String department, String address, String phoneNumber, String dateOfBirth, String collageYear, String photos, Department departments, Year year, Set<Semester> semesters) {
        this.id = id;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.email = email;
        this.department = department;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.dateOfBirth = dateOfBirth;
        this.collageYear = collageYear;
        this.photos = photos;
        this.departments = departments;
        this.year = year;
        this.semesters = semesters;
    }
}
