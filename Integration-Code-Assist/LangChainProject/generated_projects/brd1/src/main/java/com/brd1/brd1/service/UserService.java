package com.brd1.brd1.service;

import com.brd1.brd1.model.User;
import com.brd1.brd1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    // Add more service methods as needed (e.g., findUserById, deleteUser)
}