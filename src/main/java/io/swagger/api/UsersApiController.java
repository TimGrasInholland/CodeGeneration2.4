package io.swagger.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiParam;
import io.swagger.model.User;
import io.swagger.service.SessionTokenService;
import io.swagger.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-04-28T09:19:06.758Z[GMT]")
@Controller
public class UsersApiController implements UsersApi {

    private static final Logger log = LoggerFactory.getLogger(UsersApiController.class);
    private final ObjectMapper objectMapper;
    private final HttpServletRequest request;
    private UserService service;
    private Security security;
    private SessionTokenService sessionTokenService;

    @org.springframework.beans.factory.annotation.Autowired
    public UsersApiController(ObjectMapper objectMapper, HttpServletRequest request, UserService service, Security security, SessionTokenService sessionTokenService) {
        this.objectMapper = objectMapper;
        this.request = request;
        this.service = service;
        this.security = security;
        this.sessionTokenService = sessionTokenService;
    }

    // create a user
    public ResponseEntity<String> createUser(@ApiParam(value = "") @Valid @RequestBody User body) throws ParseException {
        body.setActive((true));
        // get authkey session form the website
        String authKey = request.getHeader("session");
        //check if user is employee if loged in, if not always customer
        if (!security.isPermitted(authKey, User.TypeEnum.EMPLOYEE)) {
            body.setType(User.TypeEnum.CUSTOMER);
        }
        List<User> users = service.getAllUsers();
        // checks if username already exists
        if (users.stream().anyMatch((user) -> user.getUsername().equals(body.getUsername()))) {
            return ResponseEntity.status(400).body("This username already exist");
        }
        // checks if user is older then 12
        Calendar calendar  = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -12);
        Date dateNow = calendar.getTime();
        Date birthDate= new SimpleDateFormat("yyyy-MM-dd").parse(body.getBirthdate());
        if (!birthDate.before(dateNow)) {
            return ResponseEntity.status(400).body("birthdate is to young");
        }
        if (body.getId() != null) {
            return ResponseEntity.status(400).body("No id must be given");
        } else {
            // create the user with the given object
            service.createUser(body);
            return ResponseEntity.status(HttpStatus.CREATED).body("User has been created");
        }
    }

    // get all users with params like searchname, offset and limit
    public ResponseEntity<List<User>> getAllUsers(@ApiParam(value = "get users based on searchname") @Valid @RequestParam(value = "searchname", required = false) String searchName
            , @ApiParam(value = "The number of items to skip before starting to collect the result set") @Valid @RequestParam(value = "offset", required = false) Integer offset
            , @ApiParam(value = "The numbers of items to return") @Valid @RequestParam(value = "limit", required = false) Integer limit
    ) {
        String authKey = request.getHeader("session");
        //check if user is employee
        if (security.isPermitted(authKey, User.TypeEnum.EMPLOYEE)) {
            //if the params are empty or null give all users to the employee
            if (limit == null || offset == null || limit == 0) {
                offset = 0;
                limit = 10;
                Pageable pageable = PageRequest.of(offset, limit);
                return ResponseEntity.status(200).body(service.getAllUsers(pageable));
            }
            Pageable pageable = PageRequest.of(offset, limit);
            //when there is a search string give all users that have the given string
            if (searchName != null && !searchName.isEmpty()) {
                List<User> lastnamelist = service.getAllUsersByLastname(searchName, pageable);
                List<User> usernameList = service.getAllUsersByUsername(searchName, pageable);
                // gets form the username and the lastname and checks if there is al ready the same user in the list if so then ignor the adding
                for (User user : lastnamelist) {
                    if (!usernameList.stream().anyMatch(u -> u.getId() == user.getId())) {
                        usernameList.add(user);
                    }
                }
                return ResponseEntity.status(200).body(usernameList);
            }
            return ResponseEntity.status(200).body(service.getAllUsers(pageable));
        }
        return new ResponseEntity<List<User>>(HttpStatus.UNAUTHORIZED);
    }

    // Get a unique user by id.
    public ResponseEntity<User> getUserById(@Min(1) @ApiParam(value = "", required = true, allowableValues = "") @PathVariable("id") Long id) {
        String authKey = request.getHeader("session");
        if (security.isPermitted(authKey, User.TypeEnum.CUSTOMER)) {
            // Check if the user is allowed to get his own user profile or if the user is employee.
            if (security.isOwnerOrEmployee(authKey, id)) {
                User user = service.getUserById(id);
                // Check if there is a user with given id.
                if (user == null) {
                    return new ResponseEntity<User>(HttpStatus.BAD_REQUEST);
                }
                // Check if the user is of type BANK, which is not allowed to be retrieved.
                if (security.bankCheck(user.getType())) {
                    return new ResponseEntity<User>(HttpStatus.UNAUTHORIZED);
                }
                return ResponseEntity.status(200).body(user);
            }
        }
        return new ResponseEntity<User>(HttpStatus.UNAUTHORIZED);
    }

    // update an user
    public ResponseEntity<String> updateUser(@ApiParam(value = "") @Valid @RequestBody User body) {
        String authKey = request.getHeader("session");
        // checked if user is a customer if so he can only change his own info and an employee can change that of any customer
        if (security.isPermittedAndNotBank(authKey, User.TypeEnum.CUSTOMER, body.getType())) {
            if (body != null && body.getId() != null) {
                // Check if user is owner or is employee.
                if (security.isOwnerOrEmployee(authKey, body.getId())) {

                    if (!body.isActive()) {
                        service.updateUser(body);
                        sessionTokenService.removeUserId(body.getId());
                        return ResponseEntity.status(HttpStatus.OK).body("User has been Updated");
                    }

                    // Check if the username already exists.
                    List<User> users = service.getAllUsers();
                    if (users.stream().anyMatch((user) -> user.getUsername().equals(body.getUsername())) && !(service.getUserByUsername(body.getUsername()).getId().equals(body.getId()))) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username already in use");
                    }

                    // When user is owner and is employee allow all updates except role.
                    if (security.isOwner(authKey, body.getId()) && security.employeeCheck(authKey)) {
                        body.setType(User.TypeEnum.EMPLOYEE);
                        service.updateUser(body);
                        return ResponseEntity.status(HttpStatus.OK).body("User has been Updated");
                    }
                    // When user iw owner and is customer allow all changes except active and role.
                    else if (security.isOwner(authKey, body.getId()) && security.customerCheck(authKey)) {
                        body.setType(User.TypeEnum.CUSTOMER);
                        body.setActive(true);
                        service.updateUser(body);
                        return ResponseEntity.status(HttpStatus.OK).body("User has been Updated");
                    }
                    // Else an employee is changing another use and may edit all except role.
                    else {
                        body.setType(User.TypeEnum.CUSTOMER);
                        service.updateUser(body);
                        return ResponseEntity.status(HttpStatus.OK).body("User has been Updated");
                    }

                }
                return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
            }
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body("No id was given");
        }
        return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
    }

}
