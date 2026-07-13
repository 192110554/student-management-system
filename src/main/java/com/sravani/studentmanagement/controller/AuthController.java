package com.sravani.studentmanagement.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import com.sravani.studentmanagement.dto.RegisterDTO;
import com.sravani.studentmanagement.entity.User;
import com.sravani.studentmanagement.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.Authentication;
import java.util.HashMap;
import java.util.Map;

@Controller
public class AuthController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserService userService,
                          PasswordEncoder passwordEncoder) {

        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/api/register")
    @ResponseBody
    public ResponseEntity<String> registerUser(
            @Valid @RequestBody RegisterDTO registerDTO) {

        if (userService.findByUsername(registerDTO.getUsername()).isPresent()) {
            return ResponseEntity.badRequest()
                    .body("Username already exists");
        }

        User user = new User();

        user.setUsername(registerDTO.getUsername());
        user.setPassword(registerDTO.getPassword());
        user.setRole(registerDTO.getRole());

        userService.save(user);

        return ResponseEntity.ok("User Registered Successfully");
    }

    @GetMapping("/api/current-user")
    @ResponseBody
    public ResponseEntity<User> getCurrentUser(
            org.springframework.security.core.Authentication authentication) {

        User user = userService.findByUsername(authentication.getName())
                .orElseThrow();

        return ResponseEntity.ok(user);
    }
}