package com.rl.persistence;

import com.rl.model.MyData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MyDataRepository extends JpaRepository<MyData, Long> {
}