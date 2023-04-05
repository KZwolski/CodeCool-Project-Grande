package com.codecool.CodeCoolProjectGrande.user.service;

import com.codecool.CodeCoolProjectGrande.user.User;
import com.codecool.CodeCoolProjectGrande.user.password_reset.ResetPasswordToken;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;

import java.util.UUID;

public interface ResetPasswordService {
    void sendEmail(SimpleMailMessage email);
    ResponseEntity<?> forgotPassword(String userEmail);
    void sendResetPasswordEmail(User user, ResetPasswordToken token);
    ResetPasswordToken setResetPasswordToken(User user);
    ResponseEntity<?> setNewPassword(UUID token, String password) throws JsonProcessingException;
}
