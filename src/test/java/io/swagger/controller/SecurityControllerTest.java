package io.swagger.controller;

import io.swagger.api.Security;
import io.swagger.dao.SessionTokenRepository;
import io.swagger.model.SessionToken;
import io.swagger.model.User;
import io.swagger.service.LoginService;
import io.swagger.service.SessionTokenService;
import io.swagger.service.UserService;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.BDDMockito.given;

@SpringBootTest
public class SecurityControllerTest {

    @Autowired
    private SessionTokenService sessionTokenService;

    @Autowired
    private LoginService loginService;

    private Security security;
    private SessionToken sessionToken;

    @BeforeEach
    public void setup() {
        sessionToken = new SessionToken(4L, User.TypeEnum.CUSTOMER);
    }

    @Test
    public void canRegisterSessionToken()  {
        sessionTokenService.registerSessionToken(sessionToken);
    }

    // Check if an employee can log in.
    @Test
    public void employeeCanLogin() throws IllegalArgumentException{
        User user = loginService.login("Adrie538", "Welkom123!");
        if (!user.getUsername().equals("Adrie538")){
            throw new IllegalArgumentException("Logged in user does not match credentials.");
        }
    }

    // Check if a customer can login.
    @Test
    public void customerCanLogin() throws IllegalArgumentException{
        User user = loginService.login("SjaakMaster", "Test123!");
        if (!user.getUsername().equals("SjaakMaster")){
            throw new IllegalArgumentException("Logged in user does not match credentials.");
        }
    }

    // Can get a session token by a userId.
    @Test
    public void canGetSessionTokenByUserId() throws IllegalArgumentException{
        SessionToken sessionToken = sessionTokenService.getSessionTokenByUserIdEquals(4L);
        if (sessionToken == null){
            throw new IllegalArgumentException("SessionToken could not be retrieved bij userId.");
        }
    }

    // Can get a session token by a AuthKey.
    @Test
    public void canGetSessionTokenByAuthKey() throws IllegalArgumentException{
        // Here an authKey is retrieved to test if with this authKey the session token can also be retrieved.
        SessionToken authKey = sessionTokenService.getSessionTokenByUserIdEquals(4L);
        SessionToken sessionToken = sessionTokenService.getSessionTokenByAuthKey(authKey.getAuthKey());
        if (sessionToken == null){
            throw new IllegalArgumentException("SessionToken could not be retrieved bij userId.");
        }
    }

    // Check if the employee can logout.
    @Test
    public void canLogout()  {
        sessionTokenService.logout(sessionTokenService.getSessionTokenByUserIdEquals(2L).getAuthKey());
    }

}
