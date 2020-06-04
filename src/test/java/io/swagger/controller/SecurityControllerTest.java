package io.swagger.controller;

import io.swagger.api.Security;
import io.swagger.model.SessionToken;
import io.swagger.model.User;
import io.swagger.service.LoginService;
import io.swagger.service.SessionTokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;


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

    // Check if the user can be registered and if it succeeded.
    @Test()
    public void canRegisterSessionToken() {
        sessionTokenService.registerSessionToken(sessionToken);
        //Check if the user is registered.
        SessionToken retrievedSessionToken = sessionTokenService.getSessionTokenByAuthKey(sessionToken.getAuthKey());
        assertEquals(sessionToken.getUserId(), retrievedSessionToken.getUserId());
    }

    // Check if an employee can log in.
    @Test
    public void employeeCanLogin() {
        User user = loginService.login("Adrie538", "Welkom123!");
        assertEquals("Adrie538", user.getUsername());
    }

    // Check if a customer can login.
    @Test
    public void customerCanLogin() {
        User user = loginService.login("SjaakMaster", "Test123!");
        assertEquals("SjaakMaster", user.getUsername());
    }

    // Can get a session token by a userId.
    @Test
    public void canGetSessionTokenByUserId() {
        SessionToken sessionToken = sessionTokenService.getSessionTokenByUserIdEquals(2L);
        assertNotNull(sessionToken);
    }

    // Can get a session token by a AuthKey.
    @Test
    public void canGetSessionTokenByAuthKey() {
        // Here an authKey is retrieved to test if with this authKey the session token can also be retrieved.
        SessionToken sessionTokenById = sessionTokenService.getSessionTokenByUserIdEquals(2L);
        SessionToken sessionTokenByAuthKey = sessionTokenService.getSessionTokenByAuthKey(sessionTokenById.getAuthKey());
        assertNotNull(sessionTokenByAuthKey);
    }

    // Check if the employee can logout.
    @Test
    public void canLogout() {
        sessionTokenService.logout(sessionTokenService.getSessionTokenByUserIdEquals(4L).getAuthKey());
        SessionToken sessionToken = sessionTokenService.getSessionTokenByUserIdEquals(4L);
        // Check if the user's session token is actually deleted.
        assertNull(sessionToken);
    }

}
