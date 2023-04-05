package com.codecool.CodeCoolProjectGrande.user.service.impl;

import com.codecool.CodeCoolProjectGrande.user.model.User;
import com.codecool.CodeCoolProjectGrande.user.model.UserType;
import com.codecool.CodeCoolProjectGrande.user.dto.LoginRequestDto;
import com.codecool.CodeCoolProjectGrande.user.util.EmailValidator;
import com.codecool.CodeCoolProjectGrande.user.security.UserDetailsImpl;
import com.codecool.CodeCoolProjectGrande.user.repository.UserRepository;
import com.codecool.CodeCoolProjectGrande.user.service.JwtService;
import com.codecool.CodeCoolProjectGrande.user.service.UserService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, AuthenticationManager authenticationManager, JwtService jwtService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Optional<User> getUserById(UUID id) {
        return userRepository.findUserByUserId(id);
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    @Override
    public Optional<User> getUserByToken(UUID token) {
        return userRepository.findUserByResetPasswordTokenTokenId(token);
    }

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    @Modifying
    public Optional<User> saveUser(User user) {
        userRepository.save(user);
        return Optional.of(user);
    }

    @Override
    public boolean isUserDataValid(User user){
        return EmailValidator.patternMatches(user.getEmail())
                && user.getPassword().length() >= 8
                && userRepository.findUserByEmail(user.getEmail()).isEmpty();
    }


    @Override
    public Optional<User> createUser(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setUserType(UserType.USER);
        userRepository.save(user);
        return Optional.of(user);
    }

    @Override
    public ResponseCookie authenticateUser(LoginRequestDto loginRequestDto) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequestDto.getEmail(), loginRequestDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return jwtService.generateJwtCookie(userDetails);
    }


    @NotNull
    public ResponseEntity<ResponseCookie> loginUser(LoginRequestDto loginRequestDto) {
        ResponseCookie jwtCookie = authenticateUser(loginRequestDto);
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .body(jwtCookie);
    }


    @Override
    public ResponseCookie logoutUser() {
        return jwtService.getCleanJwtCookie();
    }


    @Override
    public void deleteUser(String userEmail) {
        Optional<User> user = userRepository.findUserByEmail(userEmail);
        if(user.isPresent()){
            userRepository.removeReferenceFromAssignedUsers(String.valueOf(user.get().getUserId()));
        } else {
            System.out.println("Exception UserServiceImpl -> :101  |  w wolnej chwili ogarnąć    ↓ ↓ ↓ ↓ ↓ ↓");
        }
        userRepository.deleteUserByEmail(userEmail);
    }
}










