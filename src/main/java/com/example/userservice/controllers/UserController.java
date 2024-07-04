package com.example.userservice.controllers;

import com.example.userservice.dtos.LoginDto;
import com.example.userservice.dtos.SignupDto;
import com.example.userservice.dtos.SignupResponseDto;
import com.example.userservice.models.Token;
import com.example.userservice.models.User;
import com.example.userservice.services.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public Token login(@RequestBody LoginDto loginDto) {
        return userService.login(loginDto.getEmail(), loginDto.getPassword());
    }

    @PostMapping("/signup")
    public SignupResponseDto signup(@RequestBody SignupDto signupDto) {
        return toSignUpResponseDto(userService.signup(signupDto.getName(), signupDto.getEmail(), signupDto.getPassword()));
    }

    public SignupResponseDto toSignUpResponseDto(User user) {
        if (user == null) {
            return null; // Or throw an exception, based on your error handling policy
        }

        SignupResponseDto dto = new SignupResponseDto();
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setEmailVerified(user.isEmailVerified());

        return dto;
    }
}
