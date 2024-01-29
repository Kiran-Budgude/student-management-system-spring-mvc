package com.studentmanagementsystem.studentmanagementsystem.service;


import com.studentmanagementsystem.studentmanagementsystem.dto.UserDto;
import com.studentmanagementsystem.studentmanagementsystem.entity.User;

import java.util.List;

public interface UserService {
    void saveUser(UserDto userDto);

    User findByEmail(String email);

    List<UserDto> findAllUsers();
}
