package com.codecool.CodeCoolProjectGrande;


import com.codecool.CodeCoolProjectGrande.user.User;
import com.codecool.CodeCoolProjectGrande.user.UserType;
import com.codecool.CodeCoolProjectGrande.user.password_reset.PasswordController;
import com.codecool.CodeCoolProjectGrande.user.password_reset.PasswordServiceImpl;
import com.codecool.CodeCoolProjectGrande.user.password_reset.ResetPasswordToken;
import com.codecool.CodeCoolProjectGrande.user.repository.UserRepository;
import com.codecool.CodeCoolProjectGrande.user.service.UserServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import static org.mockito.Mockito.when;


@SpringBootTest
public class UserTests {



    @MockBean
    private UserRepository userRepository;
    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private PasswordController passwordController;

    @Autowired
    private PasswordServiceImpl passwordService;




    private final User user = User.builder().userId(UUID.randomUUID())
            .name("Test")
            .age(22)
            .email("email@gmail.com")
            .password("test1")
            .userType(UserType.USER)
            .location("Warsaw")
            .resetPasswordToken(new ResetPasswordToken())
            .build();

//User service tests

    @Test
    public void getUsersTest(){
        when(userRepository.findAll()).thenReturn(Stream.of(user, user).collect(Collectors.toList()));
        Assertions.assertEquals(2, userService.getUsers().size());
    }

    @Test
    public void getUserByEmailTest() {
        when(userRepository.findUserByEmail(user.getEmail())).thenReturn(Optional.of(user));
        Assertions.assertEquals(userService.getUserByEmail(user.getEmail()), Optional.of(user));
    }


//Password controller tests // TODO: It isn't REST controller tests, just userService tests
//Password service tests


    @Test
    public void setPasswordTokenWhenEmailExist(){
        when(userService.getUserByEmail(user.getEmail())).thenReturn(Optional.of(user));
        Assertions.assertEquals(passwordService.forgotPassword(user.getEmail()), new ResponseEntity<>(HttpStatus.OK));

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
        when(userService.getUserByToken(user.getResetPasswordToken().getTokenId())).thenReturn(Optional.of(user));
        Assertions.assertEquals(passwordService.setNewPassword(user.getResetPasswordToken().getTokenId(), newPassword), new ResponseEntity<>(HttpStatus.OK));

    }


    @Test
    public void notChangePasswordWhenTokenNotExistTest() throws JsonProcessingException {
        String newPassword = "testing";
        passwordService.setNewPassword(user.getUserId(), newPassword);
        Assertions.assertEquals(passwordService.setNewPassword(user.getUserId(), newPassword), new ResponseEntity<>(HttpStatus.NOT_FOUND));

    }

//Password controller tests

    @Test
    public void forgotPasswordPathTest() {
        when(userService.getUserByEmail(user.getEmail())).thenReturn(Optional.of(user));
        Assertions.assertEquals(passwordController.forgotPassword(user.getEmail()), new ResponseEntity<>(HttpStatus.OK));
    }

    @Test
    public void setNewPasswordPathTest() throws JsonProcessingException {
        String mockPassword = "test";
        when(userService.getUserByToken(user.getResetPasswordToken().getTokenId())).thenReturn(Optional.of(user));
        Assertions.assertEquals(passwordController.setNewPassword(user.getResetPasswordToken().getTokenId(), mockPassword), new ResponseEntity<>(HttpStatus.OK));
    }

    @Test
    public void isCreatedDateOkTest() {
        ResetPasswordToken resetPasswordToken = new ResetPasswordToken();
        Assertions.assertEquals(resetPasswordToken.getCreatedDate().getTime(), new Date().getTime());
    }

    @Test
    public void notChangePasswordWhenTokenIsExpiredTest() throws JsonProcessingException {
        ResetPasswordToken resetPasswordToken = new ResetPasswordToken();
        LocalDate pastDate = LocalDate.now().minusDays(5);
        ZoneId systemTimeZone = ZoneId.systemDefault();
        ZonedDateTime zonedDateTime = pastDate.atStartOfDay(systemTimeZone);
        Date pastUtilDate = Date.from(zonedDateTime.toInstant());
        resetPasswordToken.setCreatedDate(pastUtilDate);
        resetPasswordToken.isExpired();
        user.setResetPasswordToken(resetPasswordToken);
        when(userService.getUserByToken(user.getResetPasswordToken().getTokenId())).thenReturn(Optional.of(user));
        Assertions.assertEquals(passwordController.setNewPassword(user.getResetPasswordToken().getTokenId(), "password"), new ResponseEntity<>(HttpStatus.GONE));
    }

}
