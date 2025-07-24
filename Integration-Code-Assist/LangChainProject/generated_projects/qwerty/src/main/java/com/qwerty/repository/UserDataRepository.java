package com.qwerty.repository;

import com.qwerty.model.UserData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDataRepository extends JpaRepository<UserData, String> {
    // You can add custom query methods here if needed
}