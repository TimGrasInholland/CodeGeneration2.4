package io.swagger.controller;


import io.swagger.model.Transaction;
import io.swagger.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.threeten.bp.LocalDate;
import org.threeten.bp.OffsetDateTime;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class TransactionsControllerTest {
    @Autowired
    private TransactionService service;
    private Transaction transaction;

    @BeforeEach
    public void setup() {
        transaction = new Transaction("NL01INHO6666934694", "NL01INHO4995677694", 10.0, "description", 2L, Transaction.TransactionTypeEnum.PAYMENT);
    }

    // Test if the amount of transactions is increased after creating one
    @Test
    public void createdTransactionShouldIncreaseTransactionCount() {
        List<Transaction> transactionList = service.getAllTransactions(OffsetDateTime.MIN, OffsetDateTime.MAX, 0, 100, "%");
        int currentTransactions = transactionList.size();
        service.createTransaction(transaction);
        assertEquals(currentTransactions + 1, service.getAllTransactions(OffsetDateTime.MIN, OffsetDateTime.MAX, 0, 100, "%").size());
    }

    // Test if given limit 3, 3 transactions will be returned instead of all
    @Test
    public void getAllTransactionsWithLimit3ShouldReturn3Transactions() {
        List<Transaction> transactionList = service.getAllTransactions(OffsetDateTime.MIN, OffsetDateTime.MAX, 0, 3, "%");
        assertEquals(transactionList.size(), 3);
    }

    // Test if transactions within given timestamp are correctly returned
    @Test
    public void getAllTransactionsWithDateFromDateToShouldReturnTodayTransactions() {
        LocalDate dateToday = LocalDate.now();
        OffsetDateTime dateFrom = OffsetDateTime.parse(dateToday + "T00:00:00.001+02:00");
        OffsetDateTime dateTo = OffsetDateTime.parse(dateToday + "T23:59:59.999+02:00");

        List<Transaction> transactionList = service.getAllTransactions(dateFrom, dateTo, 0, 100, "%");
        assertEquals(transactionList.get(0).getTimestamp().getDayOfWeek(), dateToday.getDayOfWeek());
    }

}
