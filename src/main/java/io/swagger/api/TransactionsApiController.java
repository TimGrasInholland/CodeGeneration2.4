package io.swagger.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiParam;
import io.swagger.configuration.BankConfig;
import io.swagger.model.Account;
import io.swagger.model.Transaction;
import io.swagger.model.User;
import io.swagger.service.AccountBalanceService;
import io.swagger.service.AccountService;
import io.swagger.service.SessionTokenService;
import io.swagger.service.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.threeten.bp.LocalDate;
import org.threeten.bp.OffsetDateTime;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-04-28T09:19:06.758Z[GMT]")
@Controller
public class TransactionsApiController implements TransactionsApi {

    private static final Logger log = LoggerFactory.getLogger(TransactionsApiController.class);
    private final ObjectMapper objectMapper;
    private final HttpServletRequest request;
    private TransactionService service;
    private Security security;
    private SessionTokenService sessionTokenService;
    private AccountService accountService;
    private AccountBalanceService accountBalanceService;

    @org.springframework.beans.factory.annotation.Autowired
    public TransactionsApiController(ObjectMapper objectMapper, HttpServletRequest request, TransactionService service, Security security, SessionTokenService sessionTokenService, AccountService accountService, AccountBalanceService accountBalanceService) {
        this.objectMapper = objectMapper;
        this.request = request;
        this.service = service;
        this.security = security;
        this.sessionTokenService = sessionTokenService;
        this.accountService = accountService;
        this.accountBalanceService = accountBalanceService;
    }

    public ResponseEntity<String> createTransaction(@ApiParam(value = ""  )  @Valid @RequestBody Transaction body) {
        BankConfig bankConfig = new BankConfig();
        String authKey = request.getHeader("session");
        if (security.isOwnerOrEmployee(authKey, body.getUserPerformingId())) {
            if (security.isPermitted(authKey, User.TypeEnum.CUSTOMER)) {
                if (accountService.getAccountByIBAN(body.getAccountFrom()) == null || accountService.getAccountByIBAN(body.getAccountTo()) == null) { return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Given IBAN does not exist."); }
                Account accountFrom = accountService.getAccountByIBAN(body.getAccountFrom());
                Account accountTo = accountService.getAccountByIBAN(body.getAccountTo());

                // Check if role is customer and make sure accountFrom iban belongs to the current logged in user
                if (security.customerCheck(authKey) && accountFrom.getUserId() != sessionTokenService.getSessionTokenByAuthKey(authKey).getUserId()) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Can't transfer funds from someone else's to your account.");
                }
                // Check if accounts dont equal each other
                if (accountFrom == accountTo) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Can't transfer funds to the same account.");
                }
                // Currency check.
                if (accountFrom.getCurrency() != accountTo.getCurrency()) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Can't transfer funds to this Account since it has a different currency.");
                }
                // Dont let savings accounts transfer money to savings accounts.
                if (accountFrom.getType() == Account.TypeEnum.SAVINGS && accountTo.getType() == Account.TypeEnum.SAVINGS) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Can't transfer between savings accounts.");
                }
                // Check if SAVINGS withdrawals and deposits are from own user or not.
                if ((accountTo.getType().equals(Account.TypeEnum.SAVINGS) || accountFrom.getType().equals(Account.TypeEnum.SAVINGS)) && accountFrom.getUserId() != accountTo.getUserId()) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Can't withdraw from or deposit to someone else's savings account.");
                }

                Double newAmountFrom = accountFrom.getBalance().getBalance() - body.getAmount();
                Double newAmountTo = accountTo.getBalance().getBalance() + body.getAmount();

                // Absolute limit check
                if (newAmountFrom < bankConfig.getAbsoluteLimit()) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Account balance not high enough for this transaction.");
                }
                // Max amount transactions per day check
                LocalDate currentDate = LocalDate.now();
                if (service.getDailyTransactionsByUserPerforming(body.getUserPerformingId(), OffsetDateTime.parse(currentDate + "T00:00:00.001+02:00"), OffsetDateTime.parse(currentDate + "T23:59:59.999+02:00")) > bankConfig.getDayLimit()) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You've reached the maximum amount of transactions for today. Please try again tomorrow.");
                }
                // Transaction limit check
                if (body.getAmount() > bankConfig.getTransactionLimit()) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Specified transaction amount is too high.");
                }

                accountBalanceService.getAccountBalance(accountFrom.getId()).setBalance(newAmountFrom);
                accountBalanceService.getAccountBalance(accountTo.getId()).setBalance(newAmountTo);

                service.updateAccount(accountFrom);
                service.updateAccount(accountTo);
                try {
                    service.createTransaction(new Transaction(body.getAccountFrom(), body.getAccountTo(), body.getAmount(), body.getDescription(), body.getUserPerformingId(), body.getTransactionType()));
                } catch(IllegalArgumentException e) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Incorrect amount.");
                }

                return ResponseEntity.status(HttpStatus.CREATED).body("Transaction successful!");
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }

    public ResponseEntity<List<Transaction>> getAllTransactions(@ApiParam(value = "transactions to date") @Valid @RequestParam(value = "dateTo", required = false) String dateTo,@ApiParam(value = "transactions from date") @Valid @RequestParam(value = "dateFrom", required = false) String dateFrom, @ApiParam(value = "transactions from username") @Valid @RequestParam(value = "username", required = false) String username,@ApiParam(value = "The number of items to skip before starting to collect the result set") @Valid @RequestParam(value = "offset", required = false) Integer offset,@ApiParam(value = "The numbers of items to return") @Valid @RequestParam(value = "limit", required = false) Integer limit) {
        String authKey = request.getHeader("session");
        if (security.isPermitted(authKey, User.TypeEnum.EMPLOYEE)) {
        OffsetDateTime dateFromNew;
        OffsetDateTime dateToNew;

            if (dateFrom == null || dateFrom.isEmpty()){
                dateFromNew = OffsetDateTime.MIN;
            }
            else{
                dateFromNew = OffsetDateTime.parse(dateFrom + "T00:00:00.001+02:00");
            }
            if (dateTo == null || dateTo.isEmpty()){
                dateToNew = OffsetDateTime.MAX;
            }
            else{
                dateToNew = OffsetDateTime.parse(dateTo + "T23:59:59.999+02:00");
            }
            if (offset == null){
                offset = 0;
            }
            if (limit == null){
                limit = service.countAllTransactions();
            }
            if (username == null || username.isEmpty()){
                username = "%";
            }
            return ResponseEntity.status(200).body(service.getAllTransactions(dateFromNew, dateToNew, offset, limit, username));
        }
        return new ResponseEntity<List<Transaction>>(HttpStatus.UNAUTHORIZED);
    }

    public ResponseEntity<List<Transaction>> getTransactionsFromAccountId(@Min(1)@ApiParam(value = "",required=true, allowableValues="") @PathVariable("id") Long id,@ApiParam(value = "The number of items to skip before starting to collect the result set") @Valid @RequestParam(value = "offset", required = false) Integer offset,@ApiParam(value = "The numbers of items to return") @Valid @RequestParam(value = "limit", required = false) Integer limit) {
        String authKey = request.getHeader("session");
        if (security.isPermitted(authKey, User.TypeEnum.CUSTOMER)) {
            if (security.isOwnerOrEmployee(authKey, accountService.findAccountById(id).getUserId())) {
                List<Transaction> transactions;
                if ((transactions = service.getTransactionsByAccountId(id)).isEmpty()) {
                    return new ResponseEntity<List<Transaction>>(HttpStatus.NO_CONTENT);
                } else {
                    return ResponseEntity.status(200).body(transactions);
                }
            }
            return new ResponseEntity<List<Transaction>>(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<List<Transaction>>(HttpStatus.UNAUTHORIZED);
    }


    public ResponseEntity<List<Transaction>> getTransactionsFromUserId(@Min(1)@ApiParam(value = "",required=true, allowableValues="") @PathVariable("id") Long id,@ApiParam(value = "The number of items to skip before starting to collect the result set") @Valid @RequestParam(value = "offset", required = false) Integer offset,@ApiParam(value = "The numbers of items to return") @Valid @RequestParam(value = "limit", required = false) Integer limit) {
        String authKey = request.getHeader("session");
        if (security.isPermitted(authKey, User.TypeEnum.CUSTOMER)) {
            if (security.isOwnerOrEmployee(authKey, id)) {
                List<Transaction> transactions = service.getTransactionsByUserId(id);
                if (transactions.isEmpty()){
                    return new ResponseEntity<List<Transaction>>(HttpStatus.NO_CONTENT);
                } else{
                    return ResponseEntity.status(200).body(service.getTransactionsByUserId(id));
                }
            }
            return new ResponseEntity<List<Transaction>>(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<List<Transaction>>(HttpStatus.UNAUTHORIZED);
    }
}
