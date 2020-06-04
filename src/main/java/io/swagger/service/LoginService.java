package io.swagger.service;

import io.swagger.dao.LoginRepository;
import io.swagger.model.User;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    private final LoginRepository loginRepository;

    public LoginService(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    public User login(String username, String password) {
        return loginRepository.getUserByUsernameEqualsAndPasswordEqualsAndActiveIsTrue(username, password);
    }
}
