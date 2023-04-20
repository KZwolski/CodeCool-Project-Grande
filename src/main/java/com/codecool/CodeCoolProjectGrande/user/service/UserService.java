package com.codecool.CodeCoolProjectGrande.user.service;

import com.codecool.CodeCoolProjectGrande.user.dto.UserDto;
import com.codecool.CodeCoolProjectGrande.user.model.User;
import com.codecool.CodeCoolProjectGrande.user.dto.LoginRequestDto;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public interface UserService {
    Optional<User> getUserById(UUID id);
    Optional<UserDto> getUserByEmail(String email);

    ResponseEntity<ResponseCookie> loginUser(LoginRequestDto loginRequestDto);
    UserDto saveUser(User user);
    Optional<UserDto> getUserByToken(UUID token);
    List<UserDto> getUsers();
    ResponseCookie authenticateUser(LoginRequestDto loginRequestDto);
    ResponseCookie logoutUser();
    Optional<UserDto> createUser(User user);
    boolean isUserDataValid(User user);

    void deleteUser(String userEmail);


}