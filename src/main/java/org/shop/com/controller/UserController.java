package org.shop.com.controller;

import org.shop.com.entity.User;
import org.shop.com.entity.UserProfileRequest;
import org.shop.com.entity.UserRequest;
import org.shop.com.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserRequest userRequest) {
        userService.registerUser(userRequest.getName(), userRequest.getEmail(), userRequest.getPhone(), userRequest.getPassword());
        return new ResponseEntity<>("User registered successfully", HttpStatus.CREATED);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<String> updateUserProfile(@PathVariable long userId, @RequestBody UserProfileRequest userProfileRequest) {
        userService.updateUserProfile(userId, userProfileRequest.getName(), userProfileRequest.getPhone());
        return new ResponseEntity<>("User profile updated successfully", HttpStatus.OK);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUserAccount(@PathVariable long userId) {
        userService.deleteUserAccount(userId);
        return new ResponseEntity<>("User account deleted successfully", HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }
}

