package io.swagger.api;

import io.swagger.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.*;
import io.swagger.service.AccountService;
import io.swagger.service.SessionTokenService;
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
    @Autowired
    private Security security;
    @Autowired
    private SessionTokenService sessionTokenService;

    @org.springframework.beans.factory.annotation.Autowired
    public AccountsApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    public ResponseEntity<Void> createAccount(@ApiParam(value = "") @Valid @RequestBody Account body) {
        String authKey = request.getHeader("session");
        if (authKey != null && security.isPermitted(authKey, User.TypeEnum.CUSTOMER)) {
            if(body.getId() == null){
                if(body.isActive() == null){
                    body.setActive(true);
                }
                if (security.isOwner(authKey, body.getUserId()) || sessionTokenService.getSessionTokenByAuthKey(authKey).getRole().equals(User.TypeEnum.EMPLOYEE)){
                    body.setBalance(new AccountBalance(body.getUserId(), 0.00));
                    body.setIban(generateIBAN());
                    service.createAccount(body);
                    return new ResponseEntity<Void>(HttpStatus.CREATED);
                } else{
                    return new ResponseEntity<Void>(HttpStatus.NOT_ACCEPTABLE);
                }
            }
            else{
                return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
    }


    public ResponseEntity<Account> getAccountByIBAN(@ApiParam(value = "the IBAN", required = true) @PathVariable("iban") String iban) {
        String authKey = request.getHeader("session");
        if (authKey != null && security.isPermitted(authKey, User.TypeEnum.EMPLOYEE)) {
            try {
                return ResponseEntity.status(200).body(service.getAccountByIBAN(iban));
            } catch (Exception e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<Account>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return new ResponseEntity<Account>(HttpStatus.UNAUTHORIZED);
    }


    public ResponseEntity<List<Account>> getAllAccounts(@ApiParam(value = "The number of items to skip before starting to collect the result set") @Valid @RequestParam(value = "offset", required = false) Integer offset
,@ApiParam(value = "The numbers of items to return") @Valid @RequestParam(value = "limit", required = false) Integer limit, HttpServletRequest request) {
        String authKey = request.getHeader("session");
        if (authKey != null && security.isPermitted(authKey, User.TypeEnum.EMPLOYEE)) {
            if (limit != null && offset == null){
                offset = 0;
            }
            if (limit != null && offset != null && limit > 0 && offset >= 0){
                return ResponseEntity.status(200).body(service.getAllAccounts(offset, limit));
            }
            else{
                return ResponseEntity.status(200).body(service.getAllAccounts());
            }
        }
        return new ResponseEntity<List<Account>>(HttpStatus.UNAUTHORIZED);
    }

    public ResponseEntity<List<Account>> getUserAccountsByUserId(@Min(1)@ApiParam(value = "bad input parameter",required=true, allowableValues="") @PathVariable("id") Long id){
        String authKey = request.getHeader("session");
        if (authKey != null && security.isPermitted(authKey, User.TypeEnum.CUSTOMER)) {
            return ResponseEntity.status(200).body(service.getAccountsByUserId(id));
        }
        return new ResponseEntity<List<Account>>(HttpStatus.UNAUTHORIZED);
    }

    public ResponseEntity<String> disableAccount(@ApiParam(value = ""  )  @Valid @RequestBody Account body) {
        String authKey = request.getHeader("session");
        if (authKey != null && security.isPermitted(authKey, User.TypeEnum.EMPLOYEE)) {
            try {
                List<Account> accounts = service.getAccountsByUserId(body.getId());
                for (Account account : accounts) {
                    if (account.getId().equals(body.getId())){
                        // Make sure account is not changed but only set to inactive
                        body = account;
                        body.setActive(false);
                        service.disableAccount(body);
                        return ResponseEntity.status(200).body("Account disabeled succesfully");
                    }
                }
            } catch (IllegalArgumentException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
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
