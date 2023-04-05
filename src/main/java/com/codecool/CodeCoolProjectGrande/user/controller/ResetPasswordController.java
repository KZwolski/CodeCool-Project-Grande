package com.codecool.CodeCoolProjectGrande.user.controller;

import com.codecool.CodeCoolProjectGrande.user.service.impl.ResetPasswordServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;


@CrossOrigin
@RestController
@RequestMapping("/api/")
public class ResetPasswordController {

    private final ResetPasswordServiceImpl resetPasswordService;

    @Autowired
    public ResetPasswordController(ResetPasswordServiceImpl resetPasswordService) {
        this.resetPasswordService = resetPasswordService;
    }

    @PostMapping("forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody String email){
        System.out.println("testowy");
        return resetPasswordService.forgotPassword(email);

    }

    @PutMapping("reset-password")
    public ResponseEntity<?> setNewPassword(@RequestParam("token") UUID token, @RequestBody String password) throws JsonProcessingException {
        return resetPasswordService.setNewPassword(token, password);
    }
}