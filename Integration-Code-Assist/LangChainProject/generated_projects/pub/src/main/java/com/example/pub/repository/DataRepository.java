package com.example.pub.repository;

import com.example.pub.model.DataModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DataRepository extends JpaRepository<DataModel, Long> {
}