package io.swagger.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiParam;
import io.swagger.model.Account;
import io.swagger.model.AccountBalance;
import io.swagger.model.User;
import io.swagger.service.AccountService;
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
import java.util.List;
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
    @Autowired
    private UserService userService;

    @org.springframework.beans.factory.annotation.Autowired
    public AccountsApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    public ResponseEntity<Void> createAccount(@ApiParam(value = "") @Valid @RequestBody Account body) {
        String authKey = request.getHeader("session");
        if (security.isPermitted(authKey, User.TypeEnum.CUSTOMER)) {
            if(body.getId() == null){
                if(body.isActive() == null){
                    body.setActive(true);
                }
                if (security.isOwnerOrEmployee(authKey, body.getUserId())){
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
        try {
            if (security.isOwnerOrPermitted(authKey, User.TypeEnum.EMPLOYEE, service.getAccountByIBAN(iban).getUserId())){
                try {
                    // Check if the account gotten form this iban belongs to the bank and if so block this request.
                    if (!security.bankCheck(userService.getUserById(service.getAccountByIBAN(iban).getUserId()).getType())){
                        return ResponseEntity.status(200).body(service.getAccountByIBAN(iban));
                    } else{
                        return new ResponseEntity<Account>(HttpStatus.UNAUTHORIZED);
                    }
                } catch (Exception e) {
                    log.error("Couldn't serialize response for content type application/json", e);
                    return new ResponseEntity<Account>(HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
            return new ResponseEntity<Account>(HttpStatus.UNAUTHORIZED);
        } catch(NullPointerException e) {
            return new ResponseEntity<Account>(HttpStatus.BAD_REQUEST);
        }

    }


    public ResponseEntity<List<Account>> getAllAccounts(@ApiParam(value = "The number of items to skip before starting to collect the result set") @Valid @RequestParam(value = "offset", required = false) Integer offset
,@ApiParam(value = "The numbers of items to return") @Valid @RequestParam(value = "limit", required = false) Integer limit,@ApiParam(value = "The numbers of items to return") @Valid @RequestParam(value = "iban", required = false) String iban) {
        String authKey = request.getHeader("session");
        List<Account> ls = service.getAllAccounts();
        if (security.isPermitted(authKey, User.TypeEnum.EMPLOYEE)) {
            if(limit == null){
                limit = service.countAllAccounts();
            }
            if(offset == null){
                offset = 0;
            }
            if(iban == null || iban.isEmpty()){
                iban = "%";
            }
            else{
                iban = "%"+iban+"%";
            }
            Pageable pageable = PageRequest.of(offset, limit);
            return ResponseEntity.status(200).body(security.filterAccounts(service.getAllAccountsWithParams(pageable, iban)));
        }
        return new ResponseEntity<List<Account>>(HttpStatus.UNAUTHORIZED);
    }

    public ResponseEntity<List<Account>> getUserAccountsByUserId(@Min(1)@ApiParam(value = "bad input parameter",required=true, allowableValues="") @PathVariable("id") Long id){
        String authKey = request.getHeader("session");
        if (security.isPermittedAndNotBank(authKey, User.TypeEnum.EMPLOYEE, userService.getUserById(id).getType())) {
            if (security.isOwnerOrEmployee(authKey, id)){
                return ResponseEntity.status(200).body(service.getAccountsByUserId(id));
            }
        }
        return new ResponseEntity<List<Account>>(HttpStatus.UNAUTHORIZED);
    }

    public ResponseEntity<String> disableAccount(@ApiParam(value = ""  )  @Valid @RequestBody Account body) {
        String authKey = request.getHeader("session");
        if (security.isPermittedAndNotBank(authKey, User.TypeEnum.EMPLOYEE, userService.getUserById(body.getUserId()).getType())) {
            Account account = service.getAccountByIBAN(body.getIban());
            // Make sure account is not changed but only set to inactive
            body = account;
            body.setActive(false);
            service.disableAccount(body);
            return ResponseEntity.status(200).body("Account disabeled succesfully");
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
