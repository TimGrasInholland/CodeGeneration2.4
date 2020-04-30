package io.swagger.api;

import io.swagger.model.Account;
import io.swagger.model.Transaction;
import io.swagger.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.*;
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

import javax.validation.constraints.*;
import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-04-28T09:19:06.758Z[GMT]")
@Controller
public class UsersApiController implements UsersApi {

    private static final Logger log = LoggerFactory.getLogger(UsersApiController.class);
    private final ObjectMapper objectMapper;
    private final HttpServletRequest request;
    private UserService service;
    private Security security;

    @org.springframework.beans.factory.annotation.Autowired
    public UsersApiController(ObjectMapper objectMapper, HttpServletRequest request, UserService service, Security security) {
        this.objectMapper = objectMapper;
        this.request = request;
        this.service = service;
        this.security = security;
    }

    public ResponseEntity<String> createUser(@ApiParam(value = ""  )  @Valid @RequestBody User body) {
        if (body != null) {
            List<User> users = service.getAllUsers();
            if (users.stream().anyMatch((user) -> user.getUsername().equals(body.getUsername()))) {
                // maybe extra info why not correct
                return ResponseEntity.status(400).body("This username already exist");
            }
            if(body.getId() != null) {
                return ResponseEntity.status(400).body("No id must be given");
            }else
            {
                service.createUser(body);
                return ResponseEntity.status(HttpStatus.CREATED).body("User has been created");
            }
        }
        return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<List<User>> getAllUsers(@ApiParam(value = "get users based on lastname") @Valid @RequestParam(value = "lastname", required = false) String lastname
,@ApiParam(value = "get users based on username") @Valid @RequestParam(value = "username", required = false) String username
,@ApiParam(value = "The number of items to skip before starting to collect the result set") @Valid @RequestParam(value = "offset", required = false) Integer offset
,@ApiParam(value = "The numbers of items to return") @Valid @RequestParam(value = "limit", required = false) Integer limit
) {
        if(limit == null || offset == null || limit == 0 || offset == 0){
            return ResponseEntity.status(200).body(service.getAllUsers());
        }
        String authKey = request.getHeader("session");
        if (authKey != null && security.isPermitted(authKey, User.TypeEnum.EMPLOYEE)) {
            Pageable pageable = new PageRequest(offset, limit);
            if(lastname != null &&!lastname.isEmpty()){
                return ResponseEntity.status(200).body(service.getAllUsersByLastname(lastname.toLowerCase(), pageable));
            }
            if(username != null && !username.isEmpty()){
                return ResponseEntity.status(200).body(service.getAllUsersByUsername(username.toLowerCase(), pageable));
            }
            return ResponseEntity.status(200).body(service.getAllUsers(pageable));
        }
        return new ResponseEntity<List<User>>(HttpStatus.UNAUTHORIZED);
    }

    public ResponseEntity<User> getUserById(@Min(1)@ApiParam(value = "",required=true, allowableValues="") @PathVariable("id") Long id) {
        String authKey = request.getHeader("session");
        if (authKey != null && security.isPermitted(authKey, User.TypeEnum.CUSTOMER)) {
            try {
                return ResponseEntity.status(200).body(service.getUserById(id));
            } catch (IllegalArgumentException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<User>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return new ResponseEntity<User>(HttpStatus.UNAUTHORIZED);
    }

    public ResponseEntity<String> updateUser(@ApiParam(value = ""  )  @Valid @RequestBody User body) {
        String authKey = request.getHeader("session");
        if (authKey != null && security.isPermitted(authKey, User.TypeEnum.CUSTOMER)) {
            if(body != null && body.getId() != null){
                List<User> users = service.getAllUsers();
                if(users.stream().anyMatch((user) -> user.getId().equals(body.getId()))){
                    if(!users.stream().anyMatch((user) -> user.getUsername().equals(body.getUsername()))){
                        service.updateUser(body);
                        return ResponseEntity.status(HttpStatus.CREATED).body("User has been Updated");
                    }
                    //when username exits and is own id
                    if(users.stream().filter((user) -> user.getUsername().equals(body.getUsername())).findFirst().get().getId().equals(body.getId())){
                        service.updateUser(body);
                        return ResponseEntity.status(HttpStatus.CREATED).body("User has been Updated");
                    }
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username already in use");
                }
            }
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body("No id was given");
        }
        return  new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
    }

}
