package com.uw.dto;

public class LoginDTO {
    private String username;
    private String password;
    private String newPassword;

    public LoginDTO(){

    }

    public LoginDTO(String username, String password, String newPassword) {
        this.username = username;
        this.password = password;
        this.newPassword = newPassword;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
