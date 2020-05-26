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

    public List<User> getAllUsers() {return (List<User>) userRepository.findAll();}
    public List<User> getAllUsers(Pageable pageable) {
        return (List<User>) userRepository.findAll(pageable);
    }
    public void createUser(User user) {userRepository.save(user);}

    @Modifying
    public void updateUser(User user) {
        userRepository.save(user);
    }

    public User getUserById(Long id) {
        return userRepository.getUserByIdEquals(id);
    }

    public User getUserByUsername(String username) {
        return userRepository.getUserByUsernameEquals(username);
    }

    public List<User> getAllUsersByUsername(String username, Pageable pageable) {
        return userRepository.getAllByUsernameContainingIgnoreCase(username, pageable);
    }

    public List<User> getAllUsersByLastname(String lastname, Pageable pageable) {
        return userRepository.getAllByLastNameContainingIgnoreCase(lastname, pageable);
    }
}
