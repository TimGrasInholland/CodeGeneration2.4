package io.swagger.api;

import io.swagger.model.Account;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-04-23T17:10:12.432Z[GMT]")
@Controller
public class AccountsApiController implements AccountsApi {

    private static final Logger log = LoggerFactory.getLogger(AccountsApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    public AccountsApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    public ResponseEntity<Void> createAccount(@ApiParam(value = ""  )  @Valid @RequestBody Account body
) {
        String accept = request.getHeader("Accept");
        return new ResponseEntity<Void>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<List<Account>> getAccountByIBAN(@ApiParam(value = "the IBAN",required=true) @PathVariable("IBAN") String IBAN
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

    public ResponseEntity<List<Account>> getAccountByUsername(@ApiParam(value = "the username",required=true) @PathVariable("username") String username
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

    public ResponseEntity<List<Account>> getAllAccounts(@ApiParam(value = "The number of items to skip before starting to collect the result set") @Valid @RequestParam(value = "offset", required = false) Integer offset
,@ApiParam(value = "The numbers of items to return") @Valid @RequestParam(value = "limit", required = false) Integer limit
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

}
