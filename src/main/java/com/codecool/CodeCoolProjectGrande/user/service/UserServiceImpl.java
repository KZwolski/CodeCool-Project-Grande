package com.codecool.CodeCoolProjectGrande.user.service;

import com.codecool.CodeCoolProjectGrande.user.User;
import com.codecool.CodeCoolProjectGrande.user.UserType;
import com.codecool.CodeCoolProjectGrande.user.configuration.EmailValidator;
import com.codecool.CodeCoolProjectGrande.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
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

    public boolean isUserDataValid(User user){
        int MIN_PASSWORD_LENGTH = 8;
        return EmailValidator.patternMatches(user.getEmail())
                && user.getPassword().length() >= MIN_PASSWORD_LENGTH
                && userRepository.findUserByEmail(user.getEmail()).isEmpty();
    }

    public Optional<User> createUser(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setUserType(UserType.USER);
        userRepository.save(user);
        return Optional.of(user);
    }

    public Optional<User> deleteUser(String userEmail) {
        Optional<User> user = userRepository.findUserByEmail(userEmail);
        if (user.isPresent()){
            userRepository.removeReferenceFromAssignedUsers(String.valueOf(user.get().getUserId()));
            userRepository.deleteUserByEmail(userEmail);
            return user;
        }else{
            return Optional.empty();
        }
    }

}










