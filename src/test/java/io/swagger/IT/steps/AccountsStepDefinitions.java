package io.swagger.IT.steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.swagger.model.Account;
import io.swagger.model.AccountBalance;
import org.junit.Assert;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

public class AccountsStepDefinitions {

    HttpHeaders headers = new HttpHeaders();
    RestTemplate template = new RestTemplate();
    ObjectMapper mapper = new ObjectMapper();
    ResponseEntity<String> responseEntity;
    String baseUrl = "http://localhost:8080/api/Accounts";
    URI uri;

    public AccountsStepDefinitions() throws URISyntaxException {
        this.headers.add("session","testEmployee");
        this.uri = new URI(baseUrl);
    }

    @When("I retrieve all accounts")
    public void iRetrieveAllAccounts() {
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        responseEntity = template.exchange(uri, HttpMethod.GET, entity, String.class);
    }

    @When("I create an account")
    public void iCreateAccount() throws JsonProcessingException{
        Account account = new Account(2L, Account.TypeEnum.CURRENT, Account.CurrencyEnum.EUR, new AccountBalance(2L, 150.00), "NL01INHO7305732954", true);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(mapper.writeValueAsString(account), headers);
        responseEntity = template.exchange(uri, HttpMethod.POST, entity, String.class);
    }

    @When("I disable an account")
    public void iDisableAccount() throws JsonProcessingException, URISyntaxException {
        URI uri = new URI(baseUrl);
        Account account = getAccountByIban("NL01INHO6666934694");
        account.setActive(false);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(mapper.writeValueAsString(account), headers);
        responseEntity = template.exchange(uri, HttpMethod.PUT, entity, String.class);
    }

    @When("I retrieve an account by iban {string}")
    public Account getAccountByIban(String iban) throws URISyntaxException {
        URI uri = new URI(baseUrl+"/iban/"+iban);
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        responseEntity = template.exchange(uri, HttpMethod.GET, entity, String.class);
        return template.exchange(uri, HttpMethod.GET, entity, Account.class).getBody();
    }

    @When("I retrieve an account user id {int}")
    public void iGetAccountByUserId(Integer id) throws URISyntaxException{
        URI uri = new URI("http://localhost:8080/api/Users/"+id+"/Accounts");
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        responseEntity = template.exchange(uri, HttpMethod.GET, entity, String.class);
    }

    @When("I retrieve all accounts with limit {int} and offset {int}")
    public void iRetrieveAllAccountsWithParams(int limit, int offset) {
        headers.add("limit", String.valueOf(limit));
        headers.add("offset", String.valueOf(offset));
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        responseEntity = template.exchange(uri, HttpMethod.GET, entity, String.class);
    }

    @Then("I get http status account {int}")
    public void iGetHttpStatusAccounts(int status) {
        Assert.assertEquals(responseEntity.getStatusCodeValue(), status);
    }

}
