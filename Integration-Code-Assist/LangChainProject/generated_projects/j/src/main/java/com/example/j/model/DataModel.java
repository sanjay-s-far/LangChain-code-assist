package com.example.j.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "data_table") // Customize table name
public class DataModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;

    // Add more fields as needed
}