package com.codecool.CodeCoolProjectGrande.user.service;

import com.codecool.CodeCoolProjectGrande.user.dto.UserDto;
import com.codecool.CodeCoolProjectGrande.user.model.User;
import com.codecool.CodeCoolProjectGrande.user.model.ResetPasswordToken;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;

import java.util.UUID;

public interface ResetPasswordService {
    void sendEmail(SimpleMailMessage email);
    ResponseEntity<?> forgotPassword(String userEmail);
    void sendResetPasswordEmail(UserDto user, ResetPasswordToken token);
    ResetPasswordToken setResetPasswordToken(UserDto user);
    ResponseEntity<?> setNewPassword(UUID token, String password) throws JsonProcessingException;
}
