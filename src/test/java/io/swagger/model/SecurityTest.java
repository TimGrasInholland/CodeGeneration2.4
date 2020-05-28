package io.swagger.model;

import io.cucumber.java.eo.Se;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SecurityTest {

    private SessionToken sessionToken;
    private Login login;

    @BeforeEach
    public void setup() {
        sessionToken = new SessionToken(4L, User.TypeEnum.CUSTOMER);
        login = new Login();
    }

    // Check if the authKey is 36 chars like UUID should make it.
    @Test
    public void authKeyIsStrongEnough() throws IllegalArgumentException{
        if (!(sessionToken.getAuthKey().length() == 36)){
            throw new IllegalArgumentException("AuthKey is not strong enough");
        }
    }

    // Check if the session token is null
    @Test
    public void createSessionTokenShouldNotBeNull() {
        assertNotNull(sessionToken);
    }

    // Check if the login is null
    @Test
    public void createLoginShouldNotBeNull(){
        assertNotNull(login);
    }
}

