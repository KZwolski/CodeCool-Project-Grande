package com.codecool.CodeCoolProjectGrande.user.password_reset;

import com.codecool.CodeCoolProjectGrande.user.SampleUserData;
import com.codecool.CodeCoolProjectGrande.user.repository.UserRepository;
import com.codecool.CodeCoolProjectGrande.user.service.impl.ResetPasswordServiceImpl;
import com.codecool.CodeCoolProjectGrande.user.service.impl.UserServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.when;

@SpringBootTest
class ResetPasswordServiceImplTest {

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private ResetPasswordServiceImpl passwordService;

    @Test
    public void setPasswordTokenWhenEmailExist(){
        when(userService.getUserByEmail(SampleUserData.getValidUser().getEmail())).thenReturn(Optional.of(SampleUserData.getValidUser()));
        Assertions.assertEquals(passwordService.forgotPassword(SampleUserData.getValidUser().getEmail()), new ResponseEntity<>(HttpStatus.OK));

    }


    @Test
    public void passwordTokenWhenEmailNotExistTest(){
        String mockEmail = "test@test.com";
        when(userService.getUserByEmail(mockEmail)).thenReturn(Optional.empty());
        Assertions.assertEquals((passwordService.forgotPassword(mockEmail)), new ResponseEntity<>(HttpStatus.NOT_FOUND));

    }


    @Test
    public void changePasswordWhenTokenExistTest() throws JsonProcessingException {
        String newPassword = "testing";
        when(userService.getUserByToken(Mockito.any(UUID.class))).thenReturn(Optional.ofNullable(SampleUserData.getValidUser()));
        Assertions.assertEquals(passwordService.setNewPassword(SampleUserData.getValidUser().getResetPasswordToken().getTokenId(), newPassword), new ResponseEntity<>(HttpStatus.OK));

    }


    @Test
    public void notChangePasswordWhenTokenNotExistTest() throws JsonProcessingException {
        String newPassword = "testing";
        passwordService.setNewPassword(SampleUserData.getValidUser().getUserId(), newPassword);
        Assertions.assertEquals(passwordService.setNewPassword(SampleUserData.getValidUser().getUserId(), newPassword), new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

}