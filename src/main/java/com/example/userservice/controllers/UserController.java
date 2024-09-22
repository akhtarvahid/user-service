package com.example.userservice.controllers;

import com.example.userservice.dtos.LoginDto;
import com.example.userservice.dtos.LogoutDto;
import com.example.userservice.dtos.SignupDto;
import com.example.userservice.dtos.UserDto;
import com.example.userservice.models.Token;
import com.example.userservice.models.User;
import com.example.userservice.services.UserService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public UserDto signUp(@RequestBody SignupDto requestDto) {
        User user = userService.signUp(
                requestDto.getEmail(),
                requestDto.getName(),
                requestDto.getPassword()
        );

        System.out.println("Controller getting called" + user);
        return UserDto.from(user);
    }

    @PostMapping("/login")
    public Token login(@RequestBody LoginDto requestDto) {

        return userService.login(
                requestDto.getEmail(),
                requestDto.getPassword()
        );
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody LogoutDto requestDto) {
        userService.logout(requestDto.getToken());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/validate/{token}")
    public UserDto validateToken(@PathVariable String token) {
        User user = userService.validateToken(token);

        return UserDto.from(user);
    }

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable Long id) {
        System.out.println("Received request for user with id: " + id);
        return null;
    }

    @GetMapping("/all")
    public List<User> getSingleUserById() {
        List<User> users = userService.getAllUsers();

        return ResponseEntity.ok(users).getBody();
    }
}