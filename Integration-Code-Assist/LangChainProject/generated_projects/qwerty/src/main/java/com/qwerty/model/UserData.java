package com.qwerty.model;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "user_data")
public class UserData {

    @Id
    @Column(name = "user_id")
    private String userId;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "email_address")
    private String emailAddress;

    @Column(name = "signup_date")
    private Instant signupDate;

    @Column(name = "is_active")
    private boolean isActive;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public Instant getSignupDate() {
        return signupDate;
    }

    public void setSignupDate(Instant signupDate) {
        this.signupDate = signupDate;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}