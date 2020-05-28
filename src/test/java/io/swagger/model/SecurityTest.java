package io.swagger.model;

import io.cucumber.java.eo.Se;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SecurityTest {

    private SessionToken sessionToken;
    private Login login;

    @BeforeEach
    public void Setup() {
        sessionToken = new SessionToken();
        login = new Login();
    }

    @Test
    public void createSessionTokenShouldNotBeNull() {
        assertNotNull(sessionToken);
    }

    @Test
    public void createLoginShouldNotBeNull(){
        assertNotNull(login);
    }
}

