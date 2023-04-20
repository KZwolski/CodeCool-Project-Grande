package com.codecool.CodeCoolProjectGrande.user.dto;

import com.codecool.CodeCoolProjectGrande.user.model.ResetPasswordToken;
import com.codecool.CodeCoolProjectGrande.user.model.UserType;
import lombok.Data;
import java.util.UUID;

@Data
public class UserDto {
    private UUID userId;
    private String name;
    private int age;
    private String password;
    private String email;
    private UserType userType;
    private String imgUrl;
    private String location;
    private ResetPasswordToken resetPasswordToken;
}
