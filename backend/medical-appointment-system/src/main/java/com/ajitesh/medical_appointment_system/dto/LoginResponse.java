package com.ajitesh.medical_appointment_system.dto;

public class LoginResponse {
    private String message;
    private String role;
    private Integer userId;
    private String name;

    public LoginResponse(String message, String role, Integer userId, String name) {
        this.message = message;
        this.role = role;
        this.userId = userId;
        this.name = name;
    }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
