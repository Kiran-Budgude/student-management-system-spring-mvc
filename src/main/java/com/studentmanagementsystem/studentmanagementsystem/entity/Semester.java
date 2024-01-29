package com.studentmanagementsystem.studentmanagementsystem.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Semester {
    @Id
    private Long semesterId;

    private int semesterNumber;

    @OneToMany(mappedBy = "semester")
    private Set<Subject> subjects = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    // Getters and setters


    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Set<Subject> getSubjects() {
        return subjects;
    }

    public void setSubjects(Set<Subject> subjects) {
        this.subjects = subjects;
    }

    public Long getSemesterId() {
        return semesterId;
    }

    public void setSemesterId(Long semesterId) {
        this.semesterId = semesterId;
    }

    public int getSemesterNumber() {
        return semesterNumber;
    }

    public void setSemesterNumber(int semesterNumber) {
        this.semesterNumber = semesterNumber;
    }
}

