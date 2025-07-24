package com.example.j.repository;

import com.example.j.model.DataModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DataRepository extends JpaRepository<DataModel, Long> {
    // Add custom query methods here if needed
}