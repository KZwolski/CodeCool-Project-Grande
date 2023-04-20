package com.codecool.CodeCoolProjectGrande.user.service.impl;

import com.codecool.CodeCoolProjectGrande.user.dto.UserDto;
import com.codecool.CodeCoolProjectGrande.user.mapper.UserMapper;
import com.codecool.CodeCoolProjectGrande.user.model.User;
import com.codecool.CodeCoolProjectGrande.user.controller.ResetPasswordController;
import com.codecool.CodeCoolProjectGrande.user.model.ResetPasswordToken;
import com.codecool.CodeCoolProjectGrande.user.service.ResetPasswordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Optional;
import java.util.UUID;


@Service
public class ResetPasswordServiceImpl implements ResetPasswordService {
    private final UserServiceImpl userService;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;
    private static final Logger logger = LoggerFactory.getLogger(ResetPasswordController.class);


    @Autowired
    public ResetPasswordServiceImpl(UserServiceImpl userService, PasswordEncoder passwordEncoder, JavaMailSender mailSender) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.mailSender = mailSender;
    }

    public void sendEmail(SimpleMailMessage email) {
        mailSender.send(email);
    }

    public ResponseEntity<?> forgotPassword(String userEmail) {
        Optional<UserDto> user = userService.getUserByEmail(userEmail.replaceAll("\"", ""));
        if (user.isPresent()) {
            ResetPasswordToken token = setResetPasswordToken(user.get());
            sendResetPasswordEmail(user.get(), token);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public void sendResetPasswordEmail(UserDto user, ResetPasswordToken token) {
        SimpleMailMessage passwordResetEmail = new SimpleMailMessage();
        String appUrl = "http://localhost:3000";
        passwordResetEmail.setFrom("support@demo.com");
        passwordResetEmail.setTo(user.getEmail());
        passwordResetEmail.setSubject("Password Reset Request");
        passwordResetEmail.setText("To reset your password, click the link below:\n" + appUrl
                + "/reset-password/" + token.getTokenId());
        sendEmail(passwordResetEmail);
    }

    public ResetPasswordToken setResetPasswordToken(UserDto user) {
        ResetPasswordToken token = new ResetPasswordToken();
        user.setResetPasswordToken(token);
        userService.saveUser(UserMapper.mapUserToEntity(user));
        return token;
    }


    public ResponseEntity<?> setNewPassword(UUID token, String password) throws JsonProcessingException{
        Optional<UserDto> user = userService.getUserByToken(token);
        if (user.isPresent()) {
            UserDto resetUser = user.get();
            if (!resetUser.getResetPasswordToken().isExpired()) {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode jsonNode = mapper.readTree(password);
                String password1 = jsonNode.get("password").asText();
                resetUser.setPassword(passwordEncoder.encode(password1));
                resetUser.setResetPasswordToken(null);
                userService.saveUser(UserMapper.mapUserToEntity(resetUser));
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                resetUser.setResetPasswordToken(null);
                return new ResponseEntity<>(HttpStatus.GONE);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


}
