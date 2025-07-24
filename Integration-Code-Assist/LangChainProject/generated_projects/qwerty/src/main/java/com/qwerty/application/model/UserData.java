package com.qwerty.application.model;

import lombok.Data;
import java.time.Instant;
import jakarta.persistence.*;


@Data
@Entity
@Table(name = "user_data")
public class UserData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "email_address")
    private String emailAddress;

    @Column(name = "signup_date")
    private Instant signupDate;

    @Column(name = "is_active")
    private Boolean isActive;
}