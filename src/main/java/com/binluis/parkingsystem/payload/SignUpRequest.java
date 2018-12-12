package com.binluis.parkingsystem.payload;

import javax.validation.constraints.*;

public class SignUpRequest {
    @NotBlank
    @Size(min = 1, max = 40)
    private String name;

    @NotBlank
    @Size(min = 1, max = 40)
    private String username;

    @NotBlank
    @Size(max = 40)
    private String email;

    @NotBlank
    @Size(min = 1, max = 20)
    private String password;

    @Size(max = 11)
    private String phoneNumber;

    private String role;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}