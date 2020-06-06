package io.swagger.controller;

import io.swagger.model.User;
import io.swagger.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest
class UserControllerTest {
    @Autowired
    private UserService service;
    private User testUser;
    private User adrie;

    // Make users to test with
    @BeforeEach
    public void setup() {
        testUser = new User("tester123", "Test123!", "test", "Van", "test", "test@gmail.com",
                "2019-01-01", "test", "1938DR", "Amsterdam", "0638273745", User.TypeEnum.CUSTOMER, true);
        adrie = new User("Adrie538", "Welkom123!", "Andries", "", "Komen", "AndriesK@gmail.com",
                "1989-06-11", "Bloemendotter 12", "1958TX", "Haarlem", "0637291827", User.TypeEnum.EMPLOYEE, true);
        adrie.setId(2L);
    }

    // Create an new user to the list of users en test if it is created
    @Test
    public void createAnUser() {
        service.createUser(testUser);
        assertEquals(service.getUserByUsername("tester123"), testUser);
    }

    // Get all users that are in the system should be atleast one
    @Test
    public void GetAllUsers() throws Exception {
        List<User> users = service.getAllUsers();
        assertNotEquals(users.size(), 0);
    }

    // get the user defined with Id 2
    @Test
    public void GetUserByIdTwo() throws Exception {
        User test = service.getUserById((long) 2);
        assertEquals(service.getUserById((long) 2), adrie);
    }

    // get users by searching with the lastname should give one
    @Test
    public void GetUserByLastname() throws Exception {
        Pageable pageable = PageRequest.of(0, 100);
        assertNotEquals(service.getAllUsersByLastname("Komen", pageable).size(), 0);
    }

    // Get users by searching with the Username should give one
    @Test
    public void GetUserByUsername() throws Exception {
        Pageable pageable = PageRequest.of(0, 100);
        assertNotEquals(service.getAllUsersByUsername("Adrie538", pageable).size(), 0);
    }
}
