package io.swagger.api;

import io.swagger.configuration.BankConfig;
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
import org.threeten.bp.OffsetDateTime;
import java.util.List;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-04-28T09:19:06.758Z[GMT]")
@Controller
public class TransactionsApiController implements TransactionsApi {

    private static final Logger log = LoggerFactory.getLogger(TransactionsApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @Autowired
    private TransactionService service;

    @org.springframework.beans.factory.annotation.Autowired
    public TransactionsApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    public ResponseEntity<Void> createTransaction(@ApiParam(value = ""  )  @Valid @RequestBody Transaction body) {
        // TODO: regels check + waardes aanpassen
        // REGELS:
        /*
            1.	One cannot directly transfer from a savings account to an account that is not of the same customer
            2.	One cannot directly transfer to a savings account from an account that is not from the same customer.
            3.	All money flows are done with transactions, depositing and withdrawing being special cases (why?)
                - DIT BETEKENT: if (type == deposit || type == withdraw) { CHECK OF BIJDE DEZELFDE userId HEBBEN }
            4. Balance cannot become lower than a predefined number, referred to as absolute limit
            5. Cumulative transactions per day cannot surpass a predefined number, referred to as day limit
            6. The maximum amount per transaction cannot be higher than a predefined number, referred to a transaction limit
         */
        BankConfig bankConfig = new BankConfig();
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            if (body.getId() == null) {

                return new ResponseEntity<Void>(HttpStatus.CREATED);
            }
            else {
                return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<Void>(HttpStatus.NOT_IMPLEMENTED);
    }


    public ResponseEntity<List<Transaction>> getAllTransactions(@ApiParam(value = "transactions to date") @Valid @RequestParam(value = "dateTo", required = false) String dateTo,@ApiParam(value = "transactions from date") @Valid @RequestParam(value = "dateFrom", required = false) String dateFrom,@ApiParam(value = "The number of items to skip before starting to collect the result set") @Valid @RequestParam(value = "offset", required = false) Integer offset,@ApiParam(value = "The numbers of items to return") @Valid @RequestParam(value = "limit", required = false) Integer limit) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
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
        return new ResponseEntity<List<Transaction>>(HttpStatus.NOT_IMPLEMENTED);
    }

    // TODO: elke accountId return alle transactions ???????????
    public ResponseEntity<List<Transaction>> getTransactionsFromAccountId(@Min(1)@ApiParam(value = "",required=true, allowableValues="") @PathVariable("id") Long id,@ApiParam(value = "The number of items to skip before starting to collect the result set") @Valid @RequestParam(value = "offset", required = false) Integer offset,@ApiParam(value = "The numbers of items to return") @Valid @RequestParam(value = "limit", required = false) Integer limit) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            List<Transaction> transactions;
            if ((transactions = service.getAllTransactionsByAccountId(id)).isEmpty()) {
                // TODO: dit kan vast netter.... TransactionsApi..?
                return new ResponseEntity<List<Transaction>>(HttpStatus.NOT_FOUND);
            } else {
                return ResponseEntity.status(200).body(transactions);
            }
        }
        return new ResponseEntity<List<Transaction>>(HttpStatus.NOT_IMPLEMENTED);
    }


    public ResponseEntity<List<Transaction>> getTransactionsFromUserId(@Min(1)@ApiParam(value = "",required=true, allowableValues="") @PathVariable("id") Long id,@ApiParam(value = "The number of items to skip before starting to collect the result set") @Valid @RequestParam(value = "offset", required = false) Integer offset,@ApiParam(value = "The numbers of items to return") @Valid @RequestParam(value = "limit", required = false) Integer limit) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<List<Transaction>>(objectMapper.readValue("[ {\n  \"transactionType\" : \"Deposit\",\n  \"accountTo\" : \"NL01INHO0000000001\",\n  \"amount\" : 100,\n  \"userPerformingId\" : 1,\n  \"description\" : \"Money for your boat\",\n  \"id\" : 10000000001,\n  \"accountFrom\" : \"NL01INHO0000000001\",\n  \"timestamp\" : \"2020-04-21T17:32:28Z\"\n}, {\n  \"transactionType\" : \"Deposit\",\n  \"accountTo\" : \"NL01INHO0000000001\",\n  \"amount\" : 100,\n  \"userPerformingId\" : 1,\n  \"description\" : \"Money for your boat\",\n  \"id\" : 10000000001,\n  \"accountFrom\" : \"NL01INHO0000000001\",\n  \"timestamp\" : \"2020-04-21T17:32:28Z\"\n} ]", List.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<List<Transaction>>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return new ResponseEntity<List<Transaction>>(HttpStatus.NOT_IMPLEMENTED);
    }

}
