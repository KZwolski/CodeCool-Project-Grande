package com.codecool.CodeCoolProjectGrande.user.passwordreset;


import com.codecool.CodeCoolProjectGrande.user.User;
import com.codecool.CodeCoolProjectGrande.user.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@RestController
public class PasswordController {



    private EmailService emailService;
    private UserServiceImpl userServiceImpl;
    private PasswordService passwordService;

    @Autowired
    public PasswordController(EmailService emailService, UserServiceImpl userServiceImpl, PasswordService passwordService) {
        this.emailService = emailService;
        this.userServiceImpl = userServiceImpl;
        this.passwordService = passwordService;
    }

    @PostMapping("/forgot-password")
    public void forgotPassword(@RequestParam("email") String userEmail, HttpServletRequest request){
        Optional<User> user = userServiceImpl.getUserByEmail(userEmail);
        if (user.isPresent()) {
            System.out.println(user.get());
            ResetPasswordToken token = new ResetPasswordToken(user.get());
            passwordService.addToken(token);
            String appUrl = request.getScheme() + "://" + request.getServerName();
            SimpleMailMessage passwordResetEmail = new SimpleMailMessage();
            passwordResetEmail.setFrom("support@demo.com");
            passwordResetEmail.setTo(user.get().getEmail());
            passwordResetEmail.setSubject("Password Reset Request");
            passwordResetEmail.setText("To reset your password, click the link below:\n" + appUrl
                    + "/reset?token=" + token.getTokenID());
            emailService.sendEmail(passwordResetEmail);
            System.out.println(token.getTokenID());                  // TODO only one walid token
        }

    }

    @GetMapping("/reset-password")
    public void resetPasswordPage(@RequestParam("token") String token) {
        Optional<ResetPasswordToken> resetToken = passwordService.getTokenByTokenId(token);
        if (resetToken.isPresent()) { // Token found in DB
            System.out.println("Add parameter token: " + token);
        } else { // Token not found in DB
            System.out.println("error message");
        }
    }

    @PostMapping("/reset-password")     // TODO change password to request body
    public void setNewPassword(@RequestParam("token") String token, @RequestParam("password") String password){
        Optional<ResetPasswordToken> resetToken = passwordService.getTokenByTokenId(token);
        resetToken.ifPresent(resetPasswordToken -> resetPasswordToken.getUser().setPassword(password));

        }
    }
