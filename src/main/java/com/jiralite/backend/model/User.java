package com.jiralite.backend.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"org_id", "email"})
})
public class User {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "org_id", nullable = false)
    private UUID orgId;

    @Column(nullable = false)
    private String email;

    @Column(name = "password_hash", nullable = false)
    @JsonIgnore
    private String passwordHash;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "designation", nullable = false)
    private String designation;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;
    
    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }

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

    public String getPasswordHash() {
        return passwordHash;
    }
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
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

    public Boolean getIsActive() {
        return isActive;
    }
    public void setIsActive(Boolean active) {
        isActive = active;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
