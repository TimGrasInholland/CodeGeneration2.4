package io.swagger.api;

import io.swagger.configuration.BankConfig;
import io.swagger.model.User;
import io.swagger.dao.AccountRepository;
import io.swagger.model.Account;
import io.swagger.model.AccountBalance;
import io.swagger.service.AccountService;
import io.swagger.service.SessionTokenService;
import io.swagger.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import io.swagger.model.Transaction;
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

import org.threeten.bp.LocalDate;
import org.threeten.bp.OffsetDateTime;
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

    @org.springframework.beans.factory.annotation.Autowired
    public TransactionsApiController(ObjectMapper objectMapper, HttpServletRequest request, TransactionService service, Security security, SessionTokenService sessionTokenService, AccountService accountService) {
        this.objectMapper = objectMapper;
        this.request = request;
        this.service = service;
        this.security = security;
        this.sessionTokenService = sessionTokenService;
        this.accountService = accountService;
    }

    public ResponseEntity<String> createTransaction(@ApiParam(value = ""  )  @Valid @RequestBody Transaction body) {
        BankConfig bankConfig = new BankConfig();
        String authKey = request.getHeader("session");
        if (security.isOwner(authKey, body.getUserPerformingId()) || security.employeeCheck(authKey)) {
            if (authKey != null && security.isPermitted(authKey, User.TypeEnum.CUSTOMER) && body != null) {
                if (body.getTransactionType() == Transaction.TransactionTypeEnum.WITHDRAWAL) {
                    Long userFromId = accountService.getAccountByIBAN(body.getAccountFrom()).getUserId();
                    Long userToId = accountService.getAccountByIBAN(body.getAccountTo()).getUserId();
                    if (userFromId != userToId) {
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Can't withrdaw from account that isn't yours");
                    }
                }
                Account accountFrom = accountService.getAccountByIBAN(body.getAccountFrom());
                Account accountTo = accountService.getAccountByIBAN(body.getAccountTo());

                // If transaction is done by customer, check if the accounts' id matches the userPerformingId
                if (security.customerCheck(authKey)) {
                    if (accountFrom.getUserId() != body.getUserPerformingId()) {
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not permitted to do this action");
                    }
                }
                // Currency check
                if (accountFrom.getCurrency() != accountTo.getCurrency()) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Can't do transactions with account with different currency");
                }
                // Don't let savings accounts transfer money to savings accounts
                if (accountFrom.getType() == Account.TypeEnum.SAVINGS && accountTo.getType() == Account.TypeEnum.SAVINGS) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Can't do transactions between two savings accounts");
                }
                // Check if SAVINGS withdrawals and deposits are from own user or not
                if ((accountTo.getType().equals(Account.TypeEnum.SAVINGS) || accountFrom.getType().equals(Account.TypeEnum.SAVINGS)) && accountFrom.getUserId() != accountTo.getUserId()) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Savings transactions needs to be done within a users' own accounts");
                }

                Double newAmountFrom = accountFrom.getBalance().getBalance() - body.getAmount();
                Double newAmountTo = accountTo.getBalance().getBalance() + body.getAmount();

                // Check if the accounts losing it's money doesn't go below the absolute limit with this transaction
                if (newAmountFrom < bankConfig.getAbsoluteLimit()) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Account balance can't go lower than predefined absole limit");
                }
                // Check if todays transactions by this user don't pass the daily transaction limit
                LocalDate currentDate = LocalDate.now();
                if (service.getDailyTransactionsByUserPerforming(body.getUserPerformingId(), OffsetDateTime.parse(currentDate + "T00:00:00.001+02:00"), OffsetDateTime.parse(currentDate + "T23:59:59.999+02:00")) > bankConfig.getDayLimit()) {
                    return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("User's daily transactions can't pass the limit");
                }
                // Check if the amount of the transaction isn't higher than the transaction amount limit
                if (body.getAmount() > bankConfig.getTransactionLimit()) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Transaction amount can't pass the limit");
                }

                // Update both accounts accordingly
                accountFrom.getBalance().setBalance(newAmountFrom);
                accountTo.getBalance().setBalance(newAmountTo);
                service.updateAccount(accountFrom);
                service.updateAccount(accountTo);

                // Create new Transaction object to be saved in H2 DB
                service.createTransaction(new Transaction(body.getAccountFrom(), body.getAccountTo(), body.getAmount(), body.getDescription(), body.getUserPerformingId(), body.getTransactionType()));
                return ResponseEntity.status(HttpStatus.CREATED).body("Transaction successful");
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not permitted to do this action");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not permitted to do this action");
    }

    public ResponseEntity<List<Transaction>> getAllTransactions(@ApiParam(value = "transactions to date") @Valid @RequestParam(value = "dateTo", required = false) String dateTo,@ApiParam(value = "transactions from date") @Valid @RequestParam(value = "dateFrom", required = false) String dateFrom,@ApiParam(value = "The number of items to skip before starting to collect the result set") @Valid @RequestParam(value = "offset", required = false) Integer offset,@ApiParam(value = "The numbers of items to return") @Valid @RequestParam(value = "limit", required = false) Integer limit) {
        String authKey = request.getHeader("session");
        if (authKey != null && security.isPermitted(authKey, User.TypeEnum.EMPLOYEE)) {
            if (limit != null && offset == null) {
                offset = 0;
            }
            if (limit != null && offset != null && limit > 0 && offset >= 0) {
                return ResponseEntity.status(200).body(service.getAllTransactionsWithQuery(offset, limit));
            } else if (dateFrom != null && dateTo != null) {
                OffsetDateTime dateFromNew = OffsetDateTime.parse(dateFrom + "T00:00:01.001+02:00");
                OffsetDateTime dateToNew = OffsetDateTime.parse(dateTo + "T00:00:01.001+02:00");
                return ResponseEntity.status(200).body(service.getTransactionFilterDate(dateFromNew, dateToNew));
            } else {
                return ResponseEntity.status(200).body(service.getAllTransactions());
            }
        }
        return new ResponseEntity<List<Transaction>>(HttpStatus.UNAUTHORIZED);
    }

    public ResponseEntity<List<Transaction>> getTransactionsFromAccountId(@Min(1)@ApiParam(value = "",required=true, allowableValues="") @PathVariable("id") Long id,@ApiParam(value = "The number of items to skip before starting to collect the result set") @Valid @RequestParam(value = "offset", required = false) Integer offset,@ApiParam(value = "The numbers of items to return") @Valid @RequestParam(value = "limit", required = false) Integer limit) {
        String authKey = request.getHeader("session");
        if (authKey != null && security.isPermitted(authKey, User.TypeEnum.CUSTOMER)) {
            if (security.isOwner(authKey, accountService.findAccountByUserId(id).getUserId()) || security.employeeCheck(authKey)) {
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
        if (authKey != null && security.isPermitted(authKey, User.TypeEnum.CUSTOMER)) {
            if (security.isOwner(authKey, id) || security.employeeCheck(authKey)) {
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
