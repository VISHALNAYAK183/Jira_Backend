package com.jiralite.backend.model;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
public class IssueImage {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String imageUrl; // store file path or cloud URL

    @ManyToOne
    @JoinColumn(name = "issue_id")
    private Issue issue;

    // ===== Getters & Setters =====
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public Issue getIssue() { return issue; }
    public void setIssue(Issue issue) { this.issue = issue; }
}
