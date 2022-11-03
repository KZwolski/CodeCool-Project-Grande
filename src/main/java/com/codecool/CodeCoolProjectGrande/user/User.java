package com.codecool.CodeCoolProjectGrande.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@Data
public class User {
    private String userId;
    private String name;
    private int age;
    private String password;
    private String email;
    private UserType userType;
    private String imgUrl;
    private String location;
    private String resetToken;

}