package io.swagger.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiParam;
import io.swagger.configuration.BankConfig;
import io.swagger.model.Account;
import io.swagger.model.AccountBalance;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    /**
     * checks given transaction object for rule complience, updates according account balances and creates transaction object
     * @param body transaction object from front-end
     * @return ResponseEntity with user feedback
     */
    public ResponseEntity<String> createTransaction(@ApiParam(value = ""  )  @Valid @RequestBody Transaction body) {
        String authKey = request.getHeader("session");
        // security checks
        if (!security.isOwnerOrEmployee(authKey, body.getUserPerformingId()) || !security.isPermitted(authKey, User.TypeEnum.CUSTOMER))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);

        // First check is to see if given IBANs are correct
        if (accountService.getAccountByIBAN(body.getAccountFrom()) == null || accountService.getAccountByIBAN(body.getAccountTo()) == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Given IBAN does not exist.");

        // IBANs are correct, create Account objects which allow the rest of checks
        Account accountFrom = accountService.getAccountByIBAN(body.getAccountFrom());
        Account accountTo = accountService.getAccountByIBAN(body.getAccountTo());

        // Calculate new account balances
        Double newAmountFromBalance = accountFrom.getBalance().getBalance() - body.getAmount();
        Double newAmountToBalance = accountTo.getBalance().getBalance() + body.getAmount();

        // Check defined transaction "rules" by looping through set Map with rules
        String errors = "";
        Map<String, Boolean> ruleChecks = checkTransactionRules(authKey, accountFrom, accountTo, body, newAmountFromBalance);
        for (Map.Entry<String, Boolean> entry : ruleChecks.entrySet()) {
            Boolean value = entry.getValue();
            String key = entry.getKey();
            if (value) {
                errors += key+"\n";
            }
        }
        if (errors != "") {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }

        // No errors, continue
        // Update accountBalances of corresponding Accounts
        AccountBalance balanceFrom = accountBalanceService.getAccountBalance(accountFrom.getId());
        balanceFrom.setBalance(newAmountFromBalance);
        AccountBalance balanceTo = accountBalanceService.getAccountBalance(accountTo.getId());
        balanceTo.setBalance(newAmountToBalance);
        accountBalanceService.updateAccountBalance(balanceFrom);
        accountBalanceService.updateAccountBalance(balanceTo);

        // Create actual transaction object
        try {
            service.createTransaction(new Transaction(body.getAccountFrom(), body.getAccountTo(), body.getAmount(), body.getDescription(), body.getUserPerformingId(), body.getTransactionType()));
        } catch(IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Incorrect amount.");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("Transaction successful!");
    }

    /**
     * Method to check transaction to set rules
     * @param authKey
     * @param accountFrom
     * @param accountTo
     * @param body
     * @param newAmountFromBalance
     * @return map with true or false booleans, depending if transaction is correct
     */
    private Map<String, Boolean> checkTransactionRules(String authKey, Account accountFrom, Account accountTo, Transaction body, Double newAmountFromBalance) {
        BankConfig bankConfig = new BankConfig();
        Map<String, Boolean> rulesMap = new HashMap<>();

        rulesMap.put("Can't transfer funds from someone else's to your account.", security.customerCheck(authKey) && accountFrom.getUserId() != sessionTokenService.getSessionTokenByAuthKey(authKey).getUserId());
        rulesMap.put("Can't transfer funds to the same account.", accountFrom == accountTo);
        rulesMap.put("Can't transfer funds to this Account since it has a different currency.", accountFrom.getCurrency() != accountTo.getCurrency());
        rulesMap.put("Can't transfer between savings accounts.", accountFrom.getType() == Account.TypeEnum.SAVINGS && accountTo.getType() == Account.TypeEnum.SAVINGS);
        rulesMap.put("Can't withdraw from or deposit to someone else's savings account.", (accountTo.getType().equals(Account.TypeEnum.SAVINGS) || accountFrom.getType().equals(Account.TypeEnum.SAVINGS)) && accountFrom.getUserId() != accountTo.getUserId());
        rulesMap.put("Can't transfer funds to this Account since it has a different currency.", accountFrom.getCurrency() != accountTo.getCurrency());
        rulesMap.put("Account balance not high enough for this transaction.", newAmountFromBalance < bankConfig.getAbsoluteLimit());
        rulesMap.put("You've reached the maximum amount of transactions for today. Please try again tomorrow.", service.getDailyTransactionsByUserPerforming(body.getUserPerformingId(), OffsetDateTime.parse(LocalDate.now() + "T00:00:00.001+02:00"), OffsetDateTime.parse(LocalDate.now() + "T23:59:59.999+02:00")) > bankConfig.getDayLimit());
        rulesMap.put("Specified transaction amount is too high.", body.getAmount() > bankConfig.getTransactionLimit());
        return rulesMap;
    }

    /**
     * Gets all transactions with optional parameters
     * @param dateTo
     * @param dateFrom
     * @param username
     * @param offset
     * @param limit
     * @return ResponseEntity with user feedback
     */
    public ResponseEntity<List<Transaction>> getAllTransactions(@ApiParam(value = "transactions to date") @Valid @RequestParam(value = "dateTo", required = false) String dateTo,@ApiParam(value = "transactions from date") @Valid @RequestParam(value = "dateFrom", required = false) String dateFrom, @ApiParam(value = "transactions from username") @Valid @RequestParam(value = "username", required = false) String username,@ApiParam(value = "The number of items to skip before starting to collect the result set") @Valid @RequestParam(value = "offset", required = false) Integer offset,@ApiParam(value = "The numbers of items to return") @Valid @RequestParam(value = "limit", required = false) Integer limit) {
        String authKey = request.getHeader("session");
        // security check
        if (!security.isPermitted(authKey, User.TypeEnum.EMPLOYEE)) {
            return new ResponseEntity<List<Transaction>>(HttpStatus.UNAUTHORIZED);
        }
        OffsetDateTime dateFromNew;
        OffsetDateTime dateToNew;

        // creating parameters for service based on given parameters
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

    /**
     * Get all transactions with given account Id
     * @param id account Id
     * @param offset
     * @param limit
     * @return ResponseEntity with user feedback
     */
    public ResponseEntity<List<Transaction>> getTransactionsFromAccountId(@Min(1)@ApiParam(value = "",required=true, allowableValues="") @PathVariable("id") Long id,@ApiParam(value = "The number of items to skip before starting to collect the result set") @Valid @RequestParam(value = "offset", required = false) Integer offset,@ApiParam(value = "The numbers of items to return") @Valid @RequestParam(value = "limit", required = false) Integer limit) {
        String authKey = request.getHeader("session");
        // Security checks
        if (!security.isPermitted(authKey, User.TypeEnum.CUSTOMER) || !security.isOwnerOrEmployee(authKey, accountService.getAccountById(id).getUserId())) {
            return new ResponseEntity<List<Transaction>>(HttpStatus.UNAUTHORIZED);
        }

        // Get list of transactions
        List<Transaction> transactions;
        if ((transactions = service.getTransactionsByAccountId(id)).isEmpty()) {
            return new ResponseEntity<List<Transaction>>(HttpStatus.NO_CONTENT);
        } else {
            return ResponseEntity.status(200).body(transactions);
        }
    }


    /**
     * Get transactions based on user Id
     * @param id user Id
     * @param offset
     * @param limit
     * @return ResponseEntity with user feedback
     */
    public ResponseEntity<List<Transaction>> getTransactionsFromUserId(@Min(1)@ApiParam(value = "",required=true, allowableValues="") @PathVariable("id") Long id,@ApiParam(value = "The number of items to skip before starting to collect the result set") @Valid @RequestParam(value = "offset", required = false) Integer offset,@ApiParam(value = "The numbers of items to return") @Valid @RequestParam(value = "limit", required = false) Integer limit) {
        String authKey = request.getHeader("session");
        // Security checks
        if (!security.isPermitted(authKey, User.TypeEnum.CUSTOMER) || !security.isOwnerOrEmployee(authKey, id)) {
            return new ResponseEntity<List<Transaction>>(HttpStatus.UNAUTHORIZED);
        }

        // Get list of transactions
        List<Transaction> transactions = service.getTransactionsByUserId(id);
        if (transactions.isEmpty()) {
            return new ResponseEntity<List<Transaction>>(HttpStatus.NO_CONTENT);
        } else {
            return ResponseEntity.status(200).body(service.getTransactionsByUserId(id));
        }
    }
}
