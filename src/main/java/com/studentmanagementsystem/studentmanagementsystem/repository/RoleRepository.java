package com.studentmanagementsystem.studentmanagementsystem.repository;


import com.studentmanagementsystem.studentmanagementsystem.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}
