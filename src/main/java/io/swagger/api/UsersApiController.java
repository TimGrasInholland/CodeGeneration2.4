package io.swagger.api;

import io.swagger.model.Account;
import io.swagger.model.Body;
import io.swagger.model.Transaction;
import io.swagger.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    @org.springframework.beans.factory.annotation.Autowired
    public UsersApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    public ResponseEntity<Void> createUser(@ApiParam(value = ""  )  @Valid @RequestBody User body) {
        String accept = request.getHeader("Accept");
        return new ResponseEntity<Void>(HttpStatus.NOT_IMPLEMENTED);
    }


    public ResponseEntity<List<User>> getAllUsers(@ApiParam(value = "get users based on lastname") @Valid @RequestParam(value = "lastname", required = false) String lastname
,@ApiParam(value = "get users based on username") @Valid @RequestParam(value = "username", required = false) String username
,@ApiParam(value = "The number of items to skip before starting to collect the result set") @Valid @RequestParam(value = "offset", required = false) Integer offset
,@ApiParam(value = "The numbers of items to return") @Valid @RequestParam(value = "limit", required = false) Integer limit
) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<List<User>>(objectMapper.readValue("[ {\n  \"lastName\" : \"Tol\",\n  \"birthdate\" : \"2000-01-23\",\n  \"address\" : \"Fryslandlaan 12\",\n  \"city\" : \"Maaskantje\",\n  \"prefix\" : \"van\",\n  \"active\" : true,\n  \"type\" : \"Customer\",\n  \"firstName\" : \"Thijs\",\n  \"password\" : \"Welcome0!\",\n  \"phoneNumber\" : \"0612345678\",\n  \"postalcode\" : \"1902DR\",\n  \"id\" : 10000000001,\n  \"email\" : \"ThijsVanTol@gmail.com\",\n  \"username\" : \"thijs\"\n}, {\n  \"lastName\" : \"Tol\",\n  \"birthdate\" : \"2000-01-23\",\n  \"address\" : \"Fryslandlaan 12\",\n  \"city\" : \"Maaskantje\",\n  \"prefix\" : \"van\",\n  \"active\" : true,\n  \"type\" : \"Customer\",\n  \"firstName\" : \"Thijs\",\n  \"password\" : \"Welcome0!\",\n  \"phoneNumber\" : \"0612345678\",\n  \"postalcode\" : \"1902DR\",\n  \"id\" : 10000000001,\n  \"email\" : \"ThijsVanTol@gmail.com\",\n  \"username\" : \"thijs\"\n} ]", List.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<List<User>>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<List<User>>(HttpStatus.NOT_IMPLEMENTED);
    }


    public ResponseEntity<User> getUserById(@Min(1)@ApiParam(value = "",required=true, allowableValues="") @PathVariable("id") Long id) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<User>(objectMapper.readValue("{\n  \"lastName\" : \"Tol\",\n  \"birthdate\" : \"2000-01-23\",\n  \"address\" : \"Fryslandlaan 12\",\n  \"city\" : \"Maaskantje\",\n  \"prefix\" : \"van\",\n  \"active\" : true,\n  \"type\" : \"Customer\",\n  \"firstName\" : \"Thijs\",\n  \"password\" : \"Welcome0!\",\n  \"phoneNumber\" : \"0612345678\",\n  \"postalcode\" : \"1902DR\",\n  \"id\" : 10000000001,\n  \"email\" : \"ThijsVanTol@gmail.com\",\n  \"username\" : \"thijs\"\n}", User.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<User>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<User>(HttpStatus.NOT_IMPLEMENTED);
    }


    public ResponseEntity<Void> login(@ApiParam(value = "") @RequestParam(value="username", required=false)  String username
,@ApiParam(value = "") @RequestParam(value="password", required=false)  String password) {
        String accept = request.getHeader("Accept");
        return new ResponseEntity<Void>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<Void> toggleUserActive(@ApiParam(value = ""  )  @Valid @RequestBody Body body) {
        String accept = request.getHeader("Accept");
        return new ResponseEntity<Void>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<Void> updateUser(@ApiParam(value = ""  )  @Valid @RequestBody User body) {
        String accept = request.getHeader("Accept");
        return new ResponseEntity<Void>(HttpStatus.NOT_IMPLEMENTED);
    }

}
