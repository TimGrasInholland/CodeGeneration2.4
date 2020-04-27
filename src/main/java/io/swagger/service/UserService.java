package io.swagger.service;

import io.swagger.dao.TransactionRepository;
import io.swagger.dao.UserRepository;
import io.swagger.model.Transaction;
import io.swagger.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return (List<User>) userRepository.findAll();
    }

    public void deleteUserById(Integer id) {
        userRepository.deleteByIdEquals(id);
    }

    public User getUserById(Integer id) {
        return userRepository.getUserByIdEquals(id);
    }
}
