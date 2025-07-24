package com.newapplication.repository;

import com.newapplication.model.DataModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DataRepository extends JpaRepository<DataModel, Long> {
    // You can add custom query methods here if needed.
}