package io.swagger.controller;

import io.swagger.model.Account;
import io.swagger.model.User;
import io.swagger.service.AccountService;
import io.swagger.service.UserService;
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
class UserControllerTest {
    @Autowired
    @MockBean
    private UserService service;
    private List<User> users;

    @BeforeEach
    public void setup() {
        List<User> users = Arrays.asList(
                new User("Inholland-Bank", "Welcome567?", "Bank", "", "Inholland", "bank@inholland-bank.nl",
                        "2019/1/1", "Arnold straat 33", "1354PK", "Haarlem", "0638313905", User.TypeEnum.BANK, true),
                new User("Adrie538", "Welkom123!", "Andries", "", "Komen", "AndriesK@gmail.com",
                        "2019/1/1", "Bloemendotter 12", "1958TX", "Haarlem", "0637291827", User.TypeEnum.EMPLOYEE, true),
                new User("SjaakMaster", "Test123!", "Sjaak", "Van", "Bergen", "SjaakVBergen@gmail.com",
                        "2019/1/1", "Jacobstraat", "1938DR", "Amsterdam", "0638273745", User.TypeEnum.CUSTOMER, true)
        );
    }

    @Test
    public void GetAllUsers() throws Exception {
        given(service.getAllUsers()).willReturn(users);
    }

    @Test
    public void GetUserByIdTwo() throws Exception {
        given(service.getUserById((long)2)).willReturn(users.get(1));
    }

    @Test
    public void GetUserByLastname() throws Exception {
        Pageable pageable = PageRequest.of(0, 100);
        given(service.getAllUsersByLastname("Komen", pageable).get(0)).willReturn(users.get(1));
    }
}
