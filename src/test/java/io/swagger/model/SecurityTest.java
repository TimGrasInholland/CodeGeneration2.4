package io.swagger.model;


import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

public class SecurityTest {


    @BeforeEach
    public void setup() {
    }

    // Check if the authKey is 36 chars like UUID should make it.
    @Test
    public void authKeyNotCorrectLength(){
        SessionToken sessionToken = new SessionToken(4L, User.TypeEnum.CUSTOMER);
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> sessionToken.setAuthKey("123456789"));
        assertEquals("AuthKey should have 36 character according to UUID guidelines.", exception.getMessage());
    }

    // Check if the session token is null
    @Test
    public void createSessionTokenShouldNotBeNull() {
        SessionToken sessionToken = new SessionToken(4L, User.TypeEnum.CUSTOMER);
        assertNotNull(sessionToken);
    }

    // Check if the login is null
    @Test
    public void createLoginShouldNotBeNull(){
        Login login = new Login();
        assertNotNull(login);
    }
}

