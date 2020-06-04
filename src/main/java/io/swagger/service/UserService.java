package io.swagger.service;

import io.swagger.dao.UserRepository;
import io.swagger.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return (List<User>) userRepository.findAll();
    }

    public List<User> getAllUsers(Pageable pageable) {
        return userRepository.getAllByActiveIsTrue(pageable);
    }

    // redirect the user to
    public void createUser(User user) {
        userRepository.save(user);
    }

    // redirect updates for a user
    @Modifying
    public void updateUser(User user) {
        userRepository.save(user);
    }

    // redirect get user by Id
    public User getUserById(Long id) {
        return userRepository.getUserByIdEqualsAndActiveIsTrue(id);
    }

    // redirect get user by username
    public User getUserByUsername(String username) {
        return userRepository.getUserByUsernameEqualsAndActiveIsTrue(username);
    }

    // redirect get users by username
    public List<User> getAllUsersByUsername(String username, Pageable pageable) {
        return userRepository.getAllByUsernameContainingIgnoreCaseAndActiveIsTrue(username, pageable);
    }

    // redirect get users by lastname
    public List<User> getAllUsersByLastname(String lastname, Pageable pageable) {
        return userRepository.getAllByLastNameContainingIgnoreCaseAndActiveIsTrue(lastname, pageable);
    }
}
