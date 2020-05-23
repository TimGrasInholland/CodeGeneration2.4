package io.swagger.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiParam;
import io.swagger.model.User;
import io.swagger.service.SessionTokenService;
import io.swagger.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

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

    public ResponseEntity<String> createUser(@ApiParam(value = ""  )  @Valid @RequestBody User body) {
        if (body != null) {
            body.setActive(true);
            String authKey = request.getHeader("session");
            if(!security.isPermitted(authKey, User.TypeEnum.BANK)){
                body.setType(User.TypeEnum.CUSTOMER);
            }
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

    public ResponseEntity<List<User>> getAllUsers(@ApiParam(value = "get users based on searchname") @Valid @RequestParam(value = "searchname", required = false) String searchName
,@ApiParam(value = "The number of items to skip before starting to collect the result set") @Valid @RequestParam(value = "offset", required = false) Integer offset
,@ApiParam(value = "The numbers of items to return") @Valid @RequestParam(value = "limit", required = false) Integer limit
) {
        String authKey = request.getHeader("session");
        if (authKey != null && security.isPermitted(authKey, User.TypeEnum.EMPLOYEE)) {
            if(limit == null || offset == null || limit == 0 ){
                return ResponseEntity.status(200).body(service.getAllUsers());
            }
            Pageable pageable = new PageRequest(offset, limit);
            if(searchName != null &&!searchName.isEmpty()){
                List<User> lastnamelist =  service.getAllUsersByLastname(searchName, pageable);
                List<User> usernameList = service.getAllUsersByUsername(searchName, pageable);
                for(User user : lastnamelist){
                    if(!usernameList.stream().anyMatch(u -> u.getId() == user.getId())){
                        usernameList.add(user);
                    }
                }
                return ResponseEntity.status(200).body(usernameList);
            }
            return ResponseEntity.status(200).body(service.getAllUsers(pageable));
        }
        return new ResponseEntity<List<User>>(HttpStatus.UNAUTHORIZED);
    }

    public ResponseEntity<User> getUserById(@Min(1)@ApiParam(value = "",required=true, allowableValues="") @PathVariable("id") Long id) {
        String authKey = request.getHeader("session");
        if (authKey != null && security.isPermitted(authKey, User.TypeEnum.CUSTOMER)) {
            try {
                if (security.isOwner(authKey, id) || security.employeeCheck(authKey)){
                    return ResponseEntity.status(200).body(service.getUserById(id));
                }
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
            if (body != null && body.getId() != null) {
                // Check if user is owner or is employee.
                if (security.isOwner(authKey, body.getId()) || security.employeeCheck(authKey)) {

                    // Check if the username already exists.
                    List<User> users = service.getAllUsers();
                    if  (users.stream().anyMatch((user) -> user.getUsername().equals(body.getUsername())) && !(service.getUserByUsername(body.getUsername()).getId().equals(body.getId()))){
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username already in use");
                    }

                    // When user is owner and is employee allow all updates except role.
                    if (security.isOwner(authKey, body.getId()) && security.employeeCheck(authKey)) {
                        body.setType(User.TypeEnum.EMPLOYEE);
                        service.updateUser(body);
                        return ResponseEntity.status(HttpStatus.CREATED).body("User has been Updated");
                    }
                    // When user iw owner and is customer allow all changes except active and role.
                    else if (security.isOwner(authKey, body.getId()) && security.customerCheck(authKey)){
                        body.setType(User.TypeEnum.CUSTOMER);
                        body.setActive(true);
                        service.updateUser(body);
                        return ResponseEntity.status(HttpStatus.CREATED).body("User has been Updated");
                    }
                    // Else an employee is changing another use and may edit all except role.
                    else{

                        body.setType(User.TypeEnum.CUSTOMER);
                        service.updateUser(body);
                        return ResponseEntity.status(HttpStatus.CREATED).body("User has been Updated");
                    }
                }
                return  new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
            }
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body("No id was given");
        }
        return  new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
    }

}
