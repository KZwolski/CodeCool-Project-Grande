package com.codecool.CodeCoolProjectGrande.user;
import java.util.*;

public interface UserRepository {
    List<User> USERS_IN_MEMORY = new ArrayList<>(Arrays.asList(new User(UUID.randomUUID(),
            "Karol", 22, "haslo", "mejl@gmail.com", UserType.USER, "url", "Krakow", null),
        new User(UUID.randomUUID(),
                "Mateusz", 26, "haslo2", "mattwasilewski96@gmail.com", UserType.ADMIN, "url", "Warszawa", null),
        new User(UUID.randomUUID(),
                "Michal", 26, "haslo3", "michał@gmail.com", UserType.USER, "url", "Golcowa", null),
        new User(UUID.randomUUID(),
                "Bartek", 22, "haslo4", "bartek@wp.pl", UserType.MODERATOR, "url", "Jaworzno", null)));
}
