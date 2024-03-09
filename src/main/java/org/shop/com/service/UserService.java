package org.shop.com.service;

import org.shop.com.entity.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserService {
    private static final List<User> users = new ArrayList<>();

    public void registerUser(String name, String email, String phone, String password) {
        users.add(new User(name, email, phone, password));
    }

    public Optional<User> findUserByEmail(String email) {
        return users.stream().filter(user -> user.getEmail().equals(email)).findFirst();
    }

    public void updateUserProfile(long userId, String name, String phone) {
        users.stream()
                .filter(user -> user.getId() == userId)
                .findFirst()
                .ifPresent(user -> {
                    user.setName(name);
                    user.setPhone(phone);
                });
    }

    public void deleteUserAccount(long userId) {
        users.removeIf(user -> user.getId() == userId);
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(users);
    }

}

