package com.yourcompany.controllers;

import com.yourcompany.DTO.UserDTO;
import com.yourcompany.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.attribute.UserPrincipalNotFoundException;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public UserDTO registerUser(@RequestBody UserDTO userDTO) {
        userService.registerUser(userDTO);
        return userDTO;
    }

    @PostMapping("/login")
    public String loginUser(@RequestBody UserDTO userDTO) throws UserPrincipalNotFoundException {
        String token = userService.verifyUser(userDTO);
        return token;
    }
}
