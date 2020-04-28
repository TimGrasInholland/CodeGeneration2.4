package io.swagger.service;

import io.swagger.dao.TransactionRepository;
import io.swagger.dao.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
