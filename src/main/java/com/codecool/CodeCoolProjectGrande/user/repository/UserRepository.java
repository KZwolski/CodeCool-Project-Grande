package com.codecool.CodeCoolProjectGrande.user.repository;
import com.codecool.CodeCoolProjectGrande.user.User;
import com.codecool.CodeCoolProjectGrande.user.passwordreset.ResetPasswordToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.*;
@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findUserByUserId(UUID id);
    Optional<User> findUserByEmail(String email);
    Optional<User> findUserByResetPasswordTokenTokenId(UUID token);

}