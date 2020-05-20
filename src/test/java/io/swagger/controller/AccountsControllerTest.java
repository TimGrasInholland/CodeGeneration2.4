package io.swagger.controller;

import io.swagger.model.Account;
import io.swagger.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AccountsControllerTest {

    @SpringBootTest
    @AutoConfigureMockMvc
    class GuitarControllerTest {

        @Autowired
        private MockMvc mvc;

        @MockBean
        private AccountService service;
        private Account account;

        @BeforeEach
        public void setup() {
            account = new Account(2L, Account.TypeEnum.CURRENT, Account.CurrencyEnum.EUR, null, "NL01INHO8374054831", true);
        }

        /*@Test
        public void getAllAccountsShouldReturnJsonArray() throws Exception {
            given(service.getAllAccounts()).willReturn(Arrays.asList(account));
            this.mvc.perform(get("/Accounts")).andExpect(
                    status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)))
                    .andExpect(jsonPath("$[0].iban").value(account.getIban()));

        }*/
    }
}
