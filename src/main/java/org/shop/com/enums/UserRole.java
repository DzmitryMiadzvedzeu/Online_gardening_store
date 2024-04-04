package org.shop.com.enums;

public enum UserRole {

    ADMIN("Admin"),
    USER("User");

    private final String roleName;

    UserRole(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }
}