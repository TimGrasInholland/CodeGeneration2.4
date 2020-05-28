package io.swagger.controller;

import io.swagger.model.AccountBalance;
import io.swagger.model.Transaction;
import io.swagger.service.AccountBalanceService;
import io.swagger.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;

import static org.mockito.BDDMockito.given;

@SpringBootTest
public class TransactionsControllerTest {
    @Autowired
    @MockBean
    private TransactionService service;

    @Autowired
    @MockBean
    private AccountBalanceService accountBalanceService;

    private Transaction transaction;

    @BeforeEach
    public void setup() {
        transaction = new Transaction("NL01INHO6666934694", "NL01INHO4995677694", 10.0, "description", 2L, Transaction.TransactionTypeEnum.PAYMENT);
    }

    @Test
    public void getAllTransactionsShouldReturnJsonArray() {
        given(service.getAllTransactions(null, null, null, null, "")).willReturn(Arrays.asList(transaction));
    }

}
