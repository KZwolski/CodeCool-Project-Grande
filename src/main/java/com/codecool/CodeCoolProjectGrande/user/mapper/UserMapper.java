package com.codecool.CodeCoolProjectGrande.user.mapper;


import com.codecool.CodeCoolProjectGrande.user.dto.UserDto;
import com.codecool.CodeCoolProjectGrande.user.model.User;

import java.util.Optional;

public class UserMapper {
    public static UserDto mapUserToDto(User user){
            UserDto userDto = new UserDto();
            userDto.setUserId(user.getUserId());
            userDto.setName(user.getName());
            userDto.setAge(user.getAge());
            userDto.setEmail(user.getEmail());
            userDto.setPassword(user.getPassword());
            userDto.setUserType(user.getUserType());
            userDto.setImgUrl(user.getImgUrl());
            userDto.setLocation(user.getLocation());
            return userDto;
    }

    public static User mapUserToEntity(UserDto userDto){
        User user = new User();
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setAge(userDto.getAge());
        user.setPassword(userDto.getPassword());
        user.setImgUrl(userDto.getImgUrl());
        user.setUserType(userDto.getUserType());
        user.setLocation(userDto.getLocation());
        return user;
    }
}
