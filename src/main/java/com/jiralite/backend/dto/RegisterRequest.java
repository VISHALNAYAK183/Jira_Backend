package com.jiralite.backend.dto;

import java.util.UUID;

public class RegisterRequest {
    private UUID orgId;
    private String email;
    private String password;
    private String fullName;
    private String designation;

    public UUID getOrgId() {
        return orgId;
    }
    public void setOrgId(UUID orgId) {
        this.orgId = orgId;
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

    public String getFullName() {
        return fullName;
    }
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDesignation() {
        return designation;
    }
    public void setDesignation(String designation) {
        this.designation = designation;
    }
}
