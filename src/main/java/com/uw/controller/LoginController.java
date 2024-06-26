package com.uw.controller;

import com.uw.dto.LoginDTO;
import com.uw.model.User;
import com.uw.service.AuthService;
import com.uw.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("api/v1/login")
public class LoginController {

    private final UserService userService;
    @Autowired
    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<String> login(@RequestBody LoginDTO loginDTO){
        if( loginDTO.getUsername() == null || loginDTO.getPassword() == null ){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please provide a valid user and password");
        }

        if(!userService.validateUsername(loginDTO.getUsername())){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Username not found");
        }
        if (!userService.validatePassword(loginDTO.getUsername(), loginDTO.getPassword())){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Password is incorrect");
        }

        String credentials = AuthService.generateCredentials(loginDTO.getUsername(), loginDTO.getPassword());
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", credentials);
        return ResponseEntity.ok()
                .headers(headers)
                .body("Login successful");
    }

    @PutMapping
    public ResponseEntity<String> changePassword(@RequestBody LoginDTO loginDTO){
        if( loginDTO.getUsername() == null || loginDTO.getPassword() == null || loginDTO.getNewPassword() == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Make sure to send the username, old password and the new password");
        }

        if (!userService.validateUsername(loginDTO.getUsername())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Username not found");
        }
        if (!userService.validatePassword(loginDTO.getUsername(), loginDTO.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Password is incorrect");
        }

        User user = userService.findUserByUsername(loginDTO.getUsername());
        user.setPassword(loginDTO.getNewPassword());
        userService.updateUser(user);
        String credentials = AuthService.generateCredentials(loginDTO.getUsername(), loginDTO.getNewPassword());
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization",credentials);
        return ResponseEntity.ok()
                .headers(headers)
                .body("Password changed successfully");
    }

}
