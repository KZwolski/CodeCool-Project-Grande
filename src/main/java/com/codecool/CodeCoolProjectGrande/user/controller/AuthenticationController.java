package com.codecool.CodeCoolProjectGrande.user.controller;

import com.codecool.CodeCoolProjectGrande.user.model.User;
import com.codecool.CodeCoolProjectGrande.user.dto.LoginRequestDto;
import com.codecool.CodeCoolProjectGrande.user.security.ReCaptchaV3.ReCAPTCHAv3Exception;
import com.codecool.CodeCoolProjectGrande.user.security.ReCaptchaV3.ReCAPTCHAv3Response;
import com.codecool.CodeCoolProjectGrande.user.security.ReCaptchaV3.ReCAPTCHAv3Utils;
import com.codecool.CodeCoolProjectGrande.user.service.JwtService;
import com.codecool.CodeCoolProjectGrande.user.repository.UserRepository;
import com.codecool.CodeCoolProjectGrande.user.service.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;


@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final UserService userService;
    private final JwtService jwtService;
    private final static double SCORES_LEVEL = 0.7;

    public AuthenticationController(UserRepository userRepository, PasswordEncoder encoder, UserService userService, JwtService jwtService) {
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseCookie> authenticateUser(@Valid @RequestBody LoginRequestDto loginRequestDto, HttpServletRequest request) {
        String token = loginRequestDto.getToken();
        String address = request.getRemoteAddr();
        try {
            ReCAPTCHAv3Response response = ReCAPTCHAv3Utils.request(token, address);
            if (response.isSuccess()) {
                if (response.getScore() > SCORES_LEVEL) {
                    return userService.loginUser(loginRequestDto);
                } else {
                    return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
                }
            }
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (ReCAPTCHAv3Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }


    }


    @PostMapping("/login/oauth")
    public ResponseEntity<ResponseCookie> authenticateOauthUser(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        if (!userRepository.findUserByEmail(loginRequestDto.getEmail()).isPresent()){
            userRepository.save(new User(loginRequestDto.getUsername(), encoder.encode(loginRequestDto.getPassword()), loginRequestDto.getEmail()));
        }
        ResponseCookie jwtCookie = userService.authenticateUser(loginRequestDto);
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .body(jwtCookie);
    }


    @PostMapping("/logout")
    public ResponseEntity<String> logoutUser() {
        ResponseCookie cookie = jwtService.getCleanJwtCookie();
        System.out.println("wyczysciolo cookie ----------logout");
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body("You've been signed out!");
    }


}

