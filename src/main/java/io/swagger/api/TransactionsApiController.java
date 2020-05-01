package io.swagger.api;

import io.swagger.configuration.BankConfig;
import io.swagger.model.User;
import io.swagger.dao.AccountRepository;
import io.swagger.model.Account;
import io.swagger.model.AccountBalance;
import io.swagger.service.AccountService;
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

    @Autowired
    private AccountService accountService;

    @org.springframework.beans.factory.annotation.Autowired
    public TransactionsApiController(ObjectMapper objectMapper, HttpServletRequest request, TransactionService transactionService, Security security) {
        this.objectMapper = objectMapper;
        this.request = request;
        this.service = transactionService;
        this.security = security;
    }

    public ResponseEntity<Void> createTransaction(@ApiParam(value = ""  )  @Valid @RequestBody Transaction body) {
        // TODO: regels checken + CLEANUP
        /*
           RULES:
            1.	One cannot directly transfer from a savings account to an account that is not of the same customer
            2.	One cannot directly transfer to a savings account from an account that is not from the same customer.
            3.	All money flows are done with transactions, depositing and withdrawing being special cases (why?)
                - DIT BETEKENT: if (type == deposit || type == withdraw) { CHECK OF BEIDE DEZELFDE userId HEBBEN }
            4. Balance cannot become lower than a predefined number, referred to as absolute limit
            5. Cumulative transactions per day cannot surpass a predefined number, referred to as day limit
            6. The maximum amount per transaction cannot be higher than a predefined number, referred to a transaction limit
         */
        BankConfig bankConfig = new BankConfig();
        String authKey = request.getHeader("session");
        if (authKey != null && security.isPermitted(authKey, User.TypeEnum.CUSTOMER) && body != null) {
            if (body.getTransactionType() == Transaction.TransactionTypeEnum.DEPOSIT || body.getTransactionType() == Transaction.TransactionTypeEnum.WITHDRAWAL) {
                Long userFromId = service.checkUserFromId(body.getAccountFrom()).getUserPerformingId();
                Long userToId = service.checkUserToId(body.getAccountTo()).getUserPerformingId();
                if (userFromId != userToId) { return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST); }
            }
            // checken of rol customer is zoja check of accountFrom iban behoort tot de customer
            Account accountFrom = accountService.getAccountByIBAN(body.getAccountFrom());
            Account accountTo = accountService.getAccountByIBAN(body.getAccountTo());

            if (accountFrom.getCurrency() != accountTo.getCurrency()) { return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST); }

            Double newAmountFrom = accountFrom.getBalance().getBalance() - body.getAmount();
            Double newAmountTo = accountTo.getBalance().getBalance() + body.getAmount();

            if (newAmountFrom < bankConfig.getAbsoluteLimit()) { return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST); }
            LocalDate currentDate = LocalDate.now();
            if (service.getDailyTransactionsByUserPerforming(body.getUserPerformingId(), OffsetDateTime.parse(currentDate + "T00:00:00.001+02:00"), OffsetDateTime.parse(currentDate + "T23:59:59.999+02:00")) > bankConfig.getDayLimit()) { return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST); }
            if (body.getAmount() > bankConfig.getTransactionLimit()) { return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST); }

            accountFrom.getBalance().setBalance(newAmountFrom);
            accountTo.getBalance().setBalance(newAmountTo);

            service.updateAccount(accountFrom);
            service.updateAccount(accountTo);
            service.createTransaction(new Transaction(OffsetDateTime.now(), body.getAccountFrom(), body.getAccountTo(), body.getAmount(), body.getDescription(), body.getUserPerformingId(), body.getTransactionType()));
            return new ResponseEntity<Void>(HttpStatus.CREATED);
        }
        return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
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
            List<Transaction> transactions;
            if ((transactions = service.getTransactionsByAccountId(id)).isEmpty()) {
                // TODO: dit kan vast netter.... TransactionsApi..?
                return new ResponseEntity<List<Transaction>>(HttpStatus.NOT_FOUND);
            } else {
                return ResponseEntity.status(200).body(transactions);
            }
        }
        return new ResponseEntity<List<Transaction>>(HttpStatus.UNAUTHORIZED);
    }


    public ResponseEntity<List<Transaction>> getTransactionsFromUserId(@Min(1)@ApiParam(value = "",required=true, allowableValues="") @PathVariable("id") Long id,@ApiParam(value = "The number of items to skip before starting to collect the result set") @Valid @RequestParam(value = "offset", required = false) Integer offset,@ApiParam(value = "The numbers of items to return") @Valid @RequestParam(value = "limit", required = false) Integer limit) {
        String authKey = request.getHeader("session");
        if (authKey != null && security.isPermitted(authKey, User.TypeEnum.CUSTOMER)) {
            try {
                return ResponseEntity.status(200).body(service.getTransactionsByUserId(id));            }
            catch (Exception e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<List<Transaction>>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return new ResponseEntity<List<Transaction>>(HttpStatus.UNAUTHORIZED);
    }

}
