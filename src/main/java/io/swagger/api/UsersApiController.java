package io.swagger.api;

import io.swagger.model.Account;
import io.swagger.model.Transaction;
import io.swagger.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.*;
import io.swagger.service.AccountService;
import io.swagger.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;
import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-04-26T17:58:10.113Z[GMT]")
@Controller
public class UsersApiController implements UsersApi {

    private static final Logger log = LoggerFactory.getLogger(UsersApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @Autowired
    private UserService service;

    @org.springframework.beans.factory.annotation.Autowired
    public UsersApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    public ResponseEntity<Void> deleteUserById(@Min(1)@ApiParam(value = "",required=true, allowableValues="") @PathVariable("id") Integer id
) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            //return ResponseEntity.status(200).body(service.deleteUserById(id));
        }
        return new ResponseEntity<Void>(HttpStatus.NOT_IMPLEMENTED);
    }


    public ResponseEntity<List<User>> getAllUsers(@ApiParam(value = "The number of items to skip before starting to collect the result set") @Valid @RequestParam(value = "offset", required = false) Integer offset
,@ApiParam(value = "The numbers of items to return") @Valid @RequestParam(value = "limit", required = false) Integer limit
) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            return ResponseEntity.status(200).body(service.getAllUsers());
        }
        return new ResponseEntity<List<User>>(HttpStatus.NOT_IMPLEMENTED);
    }


    public ResponseEntity<List<Transaction>> getTransactionsFromUserId(@Min(1)@ApiParam(value = "",required=true, allowableValues="") @PathVariable("id") Integer id
,@ApiParam(value = "The number of items to skip before starting to collect the result set") @Valid @RequestParam(value = "offset", required = false) Integer offset
,@ApiParam(value = "The numbers of items to return") @Valid @RequestParam(value = "limit", required = false) Integer limit
) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<List<Transaction>>(objectMapper.readValue("[ {\n  \"transactionType\" : \"Deposit\",\n  \"accountTo\" : \"NL01INHO0000000001\",\n  \"amount\" : 100,\n  \"userPerforming\" : 1,\n  \"description\" : \"Money for your boat\",\n  \"id\" : 1,\n  \"accountFrom\" : \"NL01INHO0000000001\",\n  \"timestamp\" : \"2020-04-21T17:32:28Z\"\n}, {\n  \"transactionType\" : \"Deposit\",\n  \"accountTo\" : \"NL01INHO0000000001\",\n  \"amount\" : 100,\n  \"userPerforming\" : 1,\n  \"description\" : \"Money for your boat\",\n  \"id\" : 1,\n  \"accountFrom\" : \"NL01INHO0000000001\",\n  \"timestamp\" : \"2020-04-21T17:32:28Z\"\n} ]", List.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<List<Transaction>>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<List<Transaction>>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<List<Account>> getUserAccountsByUserId(@Min(1)@ApiParam(value = "bad input parameter",required=true, allowableValues="") @PathVariable("id") Integer id
) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<List<Account>>(objectMapper.readValue("[ {\n  \"balance\" : {\n    \"accountId\" : 1,\n    \"balance\" : 250,\n    \"id\" : 1\n  },\n  \"iban\" : \"NLxxINHO0xxxxxxxxx\",\n  \"currency\" : \"EUR\",\n  \"id\" : 1,\n  \"type\" : \"Savings\",\n  \"userId\" : 1\n}, {\n  \"balance\" : {\n    \"accountId\" : 1,\n    \"balance\" : 250,\n    \"id\" : 1\n  },\n  \"iban\" : \"NLxxINHO0xxxxxxxxx\",\n  \"currency\" : \"EUR\",\n  \"id\" : 1,\n  \"type\" : \"Savings\",\n  \"userId\" : 1\n} ]", List.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<List<Account>>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<List<Account>>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<List<User>> getUserById(@Min(1)@ApiParam(value = "",required=true, allowableValues="") @PathVariable("id") Integer id
) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<List<User>>(objectMapper.readValue("[ {\n  \"lastName\" : \"Tol\",\n  \"birthdate\" : \"2000-01-23\",\n  \"address\" : \"Fryslandlaan 12\",\n  \"city\" : \"Maaskantje\",\n  \"prefix\" : \"van\",\n  \"type\" : \"Customer\",\n  \"firstName\" : \"Thijs\",\n  \"password\" : \"Welcome0!\",\n  \"phoneNumber\" : \"0612345678\",\n  \"postalcode\" : \"1902DR\",\n  \"id\" : 1,\n  \"email\" : \"ThijsVanTol@gmail.com\",\n  \"username\" : \"thijs\"\n}, {\n  \"lastName\" : \"Tol\",\n  \"birthdate\" : \"2000-01-23\",\n  \"address\" : \"Fryslandlaan 12\",\n  \"city\" : \"Maaskantje\",\n  \"prefix\" : \"van\",\n  \"type\" : \"Customer\",\n  \"firstName\" : \"Thijs\",\n  \"password\" : \"Welcome0!\",\n  \"phoneNumber\" : \"0612345678\",\n  \"postalcode\" : \"1902DR\",\n  \"id\" : 1,\n  \"email\" : \"ThijsVanTol@gmail.com\",\n  \"username\" : \"thijs\"\n} ]", List.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<List<User>>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<List<User>>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<Void> login(@ApiParam(value = "") @RequestParam(value="username", required=false)  String username
,@ApiParam(value = "") @RequestParam(value="password", required=false)  String password
) {
        String accept = request.getHeader("Accept");
        return new ResponseEntity<Void>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<Void> regiserUser(@ApiParam(value = ""  )  @Valid @RequestBody User body
) {
        String accept = request.getHeader("Accept");
        return new ResponseEntity<Void>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<Void> updateUser(@ApiParam(value = ""  )  @Valid @RequestBody User body
) {
        String accept = request.getHeader("Accept");
        return new ResponseEntity<Void>(HttpStatus.NOT_IMPLEMENTED);
    }

}
