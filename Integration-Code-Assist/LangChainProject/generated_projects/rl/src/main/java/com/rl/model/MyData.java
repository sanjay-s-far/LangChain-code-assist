package com.rl.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class MyData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;

}