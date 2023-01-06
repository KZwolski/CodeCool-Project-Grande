package com.codecool.CodeCoolProjectGrande;


import com.codecool.CodeCoolProjectGrande.user.BanToken;
import com.codecool.CodeCoolProjectGrande.user.User;
import com.codecool.CodeCoolProjectGrande.user.UserType;
import com.codecool.CodeCoolProjectGrande.user.configuration.EmailValidator;
import com.codecool.CodeCoolProjectGrande.user.passwordreset.PasswordServiceImpl;
import com.codecool.CodeCoolProjectGrande.user.passwordreset.ResetPasswordToken;
import com.codecool.CodeCoolProjectGrande.user.repository.UserRepository;
import com.codecool.CodeCoolProjectGrande.user.service.UserServiceImpl;
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
import java.util.HashSet;
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
    private PasswordServiceImpl passwordService;



    private final User user = User.builder().userId(UUID.randomUUID())
            .name("Test")
            .age(22)
            .email("email@gmail.com")
            .password("test1")
            .userType(UserType.USER)
            .banToken(initBanToken())
            .location("Warsaw")
            .resetPasswordToken(new ResetPasswordToken())
            .build();


    public BanToken initBanToken(){
        BanToken banToken = new BanToken();
        banToken.setBanId(UUID.randomUUID());
        banToken.setBanDays(5);
        return banToken;
    }

    @Test
    public void saveUserTest(){
        when(userRepository.findAll()).thenReturn(Stream.of(user, user).collect(Collectors.toList()));
        Assertions.assertEquals(2, userService.getUsers().size());
    }





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
    public void changePasswordWhenTokenExistTest() {
        String newPassword = "testing";
        when(userService.getUserByToken(user.getResetPasswordToken().getTokenId())).thenReturn(Optional.of(user));
        Assertions.assertEquals(passwordService.setNewPassword(user.getResetPasswordToken().getTokenId(), newPassword), new ResponseEntity<>(HttpStatus.OK));

    }


    @Test
    public void notChangePasswordWhenTokenNotExistTest() {
        String newPassword = "testing";
        passwordService.setNewPassword(user.getUserId(), newPassword);
        Assertions.assertEquals(passwordService.setNewPassword(user.getUserId(), newPassword), new ResponseEntity<>(HttpStatus.NOT_FOUND));

    }

    @Test
    public void isEmailValidatorWorkingWithInvalidMailTest(){
        String testEmail = "sadasdasad";
        Assertions.assertFalse(EmailValidator.patternMatches(testEmail));
    }

    @Test
    public void isEmailValidatorWorkingWithValidMailTest(){
        String testEmail = "test@gmail.com";
        Assertions.assertTrue(EmailValidator.patternMatches(testEmail));
    }

    @Test
    public void isResetPasswordTokenNotExpired(){
        ResetPasswordToken resetPasswordToken = new ResetPasswordToken();
        LocalDate futureDate = LocalDate.now().plusDays(5);
        ZoneId systemTimeZone = ZoneId.systemDefault();
        ZonedDateTime zonedDateTime = futureDate.atStartOfDay(systemTimeZone);
        Date futureUtilDate = Date.from(zonedDateTime.toInstant());
        Assertions.assertTrue(resetPasswordToken.isExpired(futureUtilDate));
    }

    @Test
    public void isResetPasswordTokenExpired(){
        ResetPasswordToken resetPasswordToken = new ResetPasswordToken();
        LocalDate pastDate = LocalDate.now().minusDays(5);
        ZoneId systemTimeZone = ZoneId.systemDefault();
        ZonedDateTime zonedDateTime = pastDate.atStartOfDay(systemTimeZone);
        Date futureUtilDate = Date.from(zonedDateTime.toInstant());
        Assertions.assertFalse(resetPasswordToken.isExpired(futureUtilDate));
    }








}
