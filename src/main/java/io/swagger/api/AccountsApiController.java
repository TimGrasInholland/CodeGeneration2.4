package io.swagger.api;

import io.swagger.model.Account;
import io.swagger.model.AccountBalance;
import io.swagger.model.Transaction;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.*;
import io.swagger.service.AccountService;
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
import java.util.Random;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-04-28T09:19:06.758Z[GMT]")
@Controller
public class AccountsApiController implements AccountsApi {

    private static final Logger log = LoggerFactory.getLogger(AccountsApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @Autowired
    private AccountService service;

    @org.springframework.beans.factory.annotation.Autowired
    public AccountsApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    public ResponseEntity<Void> createAccount(@ApiParam(value = "") @Valid @RequestBody Account body) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            body.setBalance(new AccountBalance(body.getUserId(), 0.00));
            body.setIban(generateIBAN());
            service.createAccount(body);
            return new ResponseEntity<Void>(HttpStatus.CREATED);
        }
        return new ResponseEntity<Void>(HttpStatus.NOT_IMPLEMENTED);
    }


    public ResponseEntity<Account> getAccountByIBAN(@ApiParam(value = "the IBAN", required = true) @PathVariable("iban") String iban) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<Account>(objectMapper.readValue("{\n  \"balance\" : {\n    \"accountId\" : 1,\n    \"balance\" : 250,\n    \"id\" : 10000000001\n  },\n  \"iban\" : \"NLxxINHO0xxxxxxxxx\",\n  \"active\" : true,\n  \"currency\" : \"EUR\",\n  \"id\" : 10000000001,\n  \"type\" : \"Savings\",\n  \"userId\" : 1\n}", Account.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<Account>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<Account>(HttpStatus.NOT_IMPLEMENTED);
    }


    public ResponseEntity<List<Account>> getAllAccounts(@ApiParam(value = "The number of items to skip before starting to collect the result set") @Valid @RequestParam(value = "offset", required = false) Integer offset
,@ApiParam(value = "The numbers of items to return") @Valid @RequestParam(value = "limit", required = false) Integer limit, HttpServletRequest request) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                if(request.getQueryString().contains("limit") && request.getQueryString().contains("offset")){
                    return ResponseEntity.status(200).body(service.getAllAccountsWithQuery(offset, limit));
                }
                else{
                    return new ResponseEntity<List<Account>>(HttpStatus.BAD_REQUEST);
                }
            }
            catch (Exception e){
                return ResponseEntity.status(200).body(service.getAllAccounts());
            }
        }
        return new ResponseEntity<List<Account>>(HttpStatus.NOT_IMPLEMENTED);
    }


    public ResponseEntity<List<Account>> getUserAccountsByUserId(@Min(1)@ApiParam(value = "bad input parameter",required=true, allowableValues="") @PathVariable("id") Long id){
        String accept = request.getHeader("Accept");
        List<Account> accounts = service.getAccountsByUserId(id);
        if (accept != null && accept.contains("application/json") && !accounts.isEmpty()) {
            try {
                return ResponseEntity.status(200).body(accounts);
            } catch (Exception e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<List<Account>>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return new ResponseEntity<List<Account>>(HttpStatus.NOT_IMPLEMENTED);
    }

    private String generateIBAN(){
        Random rnd = new Random();
        String iban = "NL01INHO";
        for(int i = 0; i < 10; i++){
            iban += rnd.nextInt(10);
        }
        if(service.countAccountByIBAN(iban) == 0){
            return iban;
        }
        else{
            return iban = generateIBAN();
        }
    }

}
