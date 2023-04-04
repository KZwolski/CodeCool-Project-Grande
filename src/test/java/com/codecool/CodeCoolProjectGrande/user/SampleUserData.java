package com.codecool.CodeCoolProjectGrande.user;

import com.codecool.CodeCoolProjectGrande.user.password_reset.ResetPasswordToken;

import java.util.UUID;

public class SampleUserData {


    public static User getValidUser(){
        return User.builder().userId(UUID.randomUUID())
                .name("Maciek")
                .age(22)
                .email("maciek22@gmail.com")
                .password("Maciek231")
                .userType(UserType.USER)
                .location("Warsaw")
                .resetPasswordToken(new ResetPasswordToken())
                .build();
    }

    public static User getInvalidUser(){
        return User.builder().userId(UUID.randomUUID())
                .name("Tomek")
                .age(22)
                .email("tomek34@x")
                .password("tomek")
                .userType(UserType.USER)
                .location("Krakow")
                .resetPasswordToken(new ResetPasswordToken())
                .build();
    }

}
