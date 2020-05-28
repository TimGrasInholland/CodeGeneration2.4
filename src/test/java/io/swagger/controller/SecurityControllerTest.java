package io.swagger.controller;

import io.swagger.api.Security;
import io.swagger.dao.SessionTokenRepository;
import io.swagger.model.SessionToken;
import io.swagger.model.User;
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

import static org.mockito.BDDMockito.given;

@SpringBootTest
public class SecurityControllerTest {

    @Autowired
    @MockBean
    private SessionTokenRepository repository;

    @Autowired
    private SessionTokenService service;
    private Security security;
    private List<SessionToken> sessionTokens;


    @BeforeEach
    public void setup() {
        sessionTokens = Arrays.asList(
                new SessionToken("1234567890", 3L, User.TypeEnum.CUSTOMER),
                new SessionToken("1234567890", 1L, User.TypeEnum.BANK),
                new SessionToken("1234567890", 2L, User.TypeEnum.EMPLOYEE)
        );

        sessionTokens.forEach(
                repository::save
        );
    }

    @Test
    public void CanRegisterSessionToken()  {
        service.registerSessionToken(sessionTokens.get(0));
    }

    @Test
    public void CanLogout()  {
        service.logout(sessionTokens.get(0).getAuthKey());
    }
}
