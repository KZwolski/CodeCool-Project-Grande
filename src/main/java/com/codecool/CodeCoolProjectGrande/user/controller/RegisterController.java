package com.codecool.CodeCoolProjectGrande.user.controller;

import com.codecool.CodeCoolProjectGrande.user.UserType;
import com.codecool.CodeCoolProjectGrande.user.configuration.EmailValidator;
import com.codecool.CodeCoolProjectGrande.user.configuration.SecurityConfig;
import com.codecool.CodeCoolProjectGrande.user.User;
import com.codecool.CodeCoolProjectGrande.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.UUID;

@Controller
@CrossOrigin
@RequestMapping("/api")
public class RegisterController {
    private final UserRepository userRepository;
    private final SecurityConfig securityConfig;

    @Autowired
    public RegisterController(UserRepository userRepository, SecurityConfig securityConfig) {
        this.userRepository = userRepository;
        this.securityConfig = securityConfig;
    }

    @PostMapping("/registration")
    public ResponseEntity<?> registerAccount(@RequestBody User user){
        //TODO to do servisu
        if (EmailValidator.patternMatches(user.getEmail()) && user.getPassword().length() >= 8){
            user.setPassword(securityConfig.passwordEncoder().encode(user.getPassword()));
            user.setUserType(UserType.USER);
            userRepository.save(user);
        }
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
