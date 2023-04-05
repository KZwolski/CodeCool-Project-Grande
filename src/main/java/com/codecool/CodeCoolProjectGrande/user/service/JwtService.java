package com.codecool.CodeCoolProjectGrande.user.service;

import org.springframework.http.ResponseCookie;
import org.springframework.security.core.userdetails.UserDetails;

import javax.servlet.http.HttpServletRequest;

public interface JwtService {
    String getJwtFromCookies(HttpServletRequest request);
    ResponseCookie generateJwtCookie(UserDetails userPrincipal);
    ResponseCookie getCleanJwtCookie();
    String getUserNameFromJwtToken(String token);
    boolean validateJwtToken(String authToken);
    String generateTokenFromUsername(String username);
}
