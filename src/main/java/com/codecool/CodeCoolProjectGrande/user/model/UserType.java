package com.codecool.CodeCoolProjectGrande.user.model;

public enum UserType {
    USER("USER"),
    MODERATOR("MODERATOR"),
    ADMIN("ADMIN");

    private final String role;

    UserType(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    @Override
    public String toString() {
        return role;
    }


}
