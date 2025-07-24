package com.jh.repository;

import com.jh.model.SomeData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SomeDataRepository extends JpaRepository<SomeData, Long> {
}