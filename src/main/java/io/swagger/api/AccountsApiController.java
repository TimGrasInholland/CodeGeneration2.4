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

    /**
     * Get all accounts with optional query parameters
     * @param offset
     * @param limit
     * @param iban
     * @return HttpStatus
     */
    public ResponseEntity<List<Account>> getAllAccounts(@ApiParam(value = "The number of items to skip before starting to collect the result set") @Valid @RequestParam(value = "offset", required = false) Integer offset
            , @ApiParam(value = "The numbers of items to return") @Valid @RequestParam(value = "limit", required = false) Integer limit, @ApiParam(value = "The numbers of items to return") @Valid @RequestParam(value = "iban", required = false) String iban) {
        String authKey = request.getHeader("session");
        if (security.isPermitted(authKey, User.TypeEnum.EMPLOYEE)) {
            //Set standard values if not given in query parameters
            if (limit == null)
                limit = 50;
            if (offset == null)
                offset = 0;
            if (iban == null || iban.isEmpty())
                iban = "%";
            else
                iban = "%" + iban + "%";
            Pageable pageable = PageRequest.of(offset, limit);
            return ResponseEntity.status(200).body(security.filterAccounts(service.getAllAccountsWithParams(pageable, iban)));
        }
        return new ResponseEntity<List<Account>>(HttpStatus.UNAUTHORIZED);
    }

    /**
     * Get an account by IBAN given in the URL parameter
     * @param iban
     * @return HttpStatus
     */
    public ResponseEntity<Account> getAccountByIBAN(@ApiParam(value = "the IBAN", required = true) @PathVariable("iban") String iban) {
        String authKey = request.getHeader("session");
        try {
            Account account = service.getAccountByIBAN(iban);
            if (security.isOwnerOrPermitted(authKey, User.TypeEnum.EMPLOYEE, account.getUserId())) {
                // Check if the account gotten form this iban belongs to the bank and if so block this request.
                if (!security.bankCheck(userService.getUserById(account.getUserId()).getType())) {
                    return ResponseEntity.status(200).body(account);
                } else {
                    return new ResponseEntity<Account>(HttpStatus.UNAUTHORIZED);
                }
            }
            return new ResponseEntity<Account>(HttpStatus.UNAUTHORIZED);
        } catch (NullPointerException e) {
            //Catch NullPointerExeption if IBAN does not exists in database
            return new ResponseEntity<Account>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Get accounts by the give userId in the URL parameter
     * @param id
     * @return HttpStatus
     */
    public ResponseEntity<List<Account>> getUserAccountsByUserId(@Min(1) @ApiParam(value = "bad input parameter", required = true, allowableValues = "") @PathVariable("id") Long id) {
        String authKey = request.getHeader("session");
        try {
            if (security.isPermittedAndNotBank(authKey, User.TypeEnum.CUSTOMER, userService.getUserById(id).getType())) {
                if (security.isOwnerOrEmployee(authKey, id)) {
                    return ResponseEntity.status(200).body(service.getAccountsByUserId(id));
                }
            }
            return new ResponseEntity<List<Account>>(HttpStatus.UNAUTHORIZED);
        } catch (NullPointerException e) {
            //Catch NullPointerExeption if user does not exists in database
            return new ResponseEntity<List<Account>>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Create an account for an user. Only needed fields are: userId, Type and Currency
     * @param body
     * @return HttpStatus
     */
    public ResponseEntity<Void> createAccount(@ApiParam(value = "") @Valid @RequestBody Account body) {
        String authKey = request.getHeader("session");
        if (security.isPermitted(authKey, User.TypeEnum.CUSTOMER)) {
            if (body.getId() == null) {
                if (security.isOwnerOrEmployee(authKey, body.getUserId())) {
                    body.setIban(generateIBAN());
                    body.setActive(true);
                    // create account with empty AccountBalance because accountId gets set with creation
                    service.createAccount(body);
                    // get complete account with id
                    Account acc = service.getAccountByIBAN(body.getIban());
                    // set balance know that we know id
                    acc.setBalance(new AccountBalance(acc.getId(), 0.00));
                    // update acc with balance obj
                    service.createAccount(acc);
                    return new ResponseEntity<Void>(HttpStatus.CREATED);
                } else {
                    return new ResponseEntity<Void>(HttpStatus.NOT_ACCEPTABLE);
                }
            } else {
                return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
    }

    /**
     * The account of a user can be disabled but only if the acting user is an Employee.
     * @param body
     * @return HttpStatus
     */
    public ResponseEntity<String> disableAccount(@ApiParam(value = "") @Valid @RequestBody Account body) {
        String authKey = request.getHeader("session");
        if (security.isPermittedAndNotBank(authKey, User.TypeEnum.EMPLOYEE, userService.getUserById(body.getUserId()).getType())) {
            Account account = service.getAccountByIBAN(body.getIban());
            // Make sure account is not changed but only set to inactive
            account.setActive(false);
            service.disableAccount(account);
            return ResponseEntity.status(200).body("Account disabeled succesfully");
        }
        return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
    }

    /**
     * Generate a random IBAN for a new account
     * @return iban
     */
    private String generateIBAN() {
        Random rnd = new Random();
        String iban = "NL01INHO";
        for (int i = 0; i < 10; i++) {
            iban += rnd.nextInt(10);
        }
        //Check if generated iban already exists
        if (service.countAccountByIBAN(iban) == 0) {
            return iban;
        } else {
            return iban = generateIBAN();
        }
    }
}
