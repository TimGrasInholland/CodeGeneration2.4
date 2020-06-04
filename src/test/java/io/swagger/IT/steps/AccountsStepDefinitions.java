package io.swagger.IT.steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.swagger.IT.BaseClassTesting;
import io.swagger.model.Account;
import org.junit.Assert;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

import java.net.URI;
import java.net.URISyntaxException;

public class AccountsStepDefinitions extends BaseClassTesting {

    @When("I retrieve all accounts")
    public void iRetrieveAllAccounts() throws URISyntaxException {
        uri = new URI(baseUrl + "/Accounts");
        httpEntity = new HttpEntity<>(null, headers);
        responseEntity = template.exchange(uri, HttpMethod.GET, httpEntity, String.class);
    }

    @When("I create an account")
    public void iCreateAccount() throws URISyntaxException {
        uri = new URI(baseUrl + "/Accounts");
        headers.setContentType(MediaType.APPLICATION_JSON);
        String account = "{\"userId\":2,\"type\":\"Current\",\"currency\":\"EUR\"}";
        httpEntity = new HttpEntity<>(account, headers);
        responseEntity = template.exchange(uri, HttpMethod.POST, httpEntity, String.class);
    }

    @When("I disable an account")
    public void iDisableAccount() throws URISyntaxException, JsonProcessingException {
        Account account = getAccountByIban("NL01INHO6666934694");
        account.setActive(false);
        uri = new URI(baseUrl + "/Accounts");
        headers.setContentType(MediaType.APPLICATION_JSON);
        httpEntity = new HttpEntity<>(mapper.writeValueAsString(account), headers);
        responseEntity = template.exchange(uri, HttpMethod.PUT, httpEntity, String.class);
    }

    @When("I retrieve an account by iban {string}")
    public Account getAccountByIban(String iban) throws URISyntaxException {
        uri = new URI(baseUrl + "/Accounts/iban/" + iban);
        httpEntity = new HttpEntity<>(null, headers);
        responseEntity = template.exchange(uri, HttpMethod.GET, httpEntity, String.class);
        return template.exchange(uri, HttpMethod.GET, httpEntity, Account.class).getBody();
    }

    @When("I retrieve an account user id {int}")
    public void iGetAccountByUserId(Integer id) throws URISyntaxException {
        uri = new URI(baseUrl + "/Users/" + id + "/Accounts");
        httpEntity = new HttpEntity<>(null, headers);
        responseEntity = template.exchange(uri, HttpMethod.GET, httpEntity, String.class);
    }

    @When("I retrieve all accounts with limit {int} and offset {int}")
    public void iRetrieveAllAccountsWithParams(int limit, int offset) throws URISyntaxException {
        uri = new URI(baseUrl + "/Accounts");
        headers.add("limit", String.valueOf(limit));
        headers.add("offset", String.valueOf(offset));
        httpEntity = new HttpEntity<>(null, headers);
        responseEntity = template.exchange(uri, HttpMethod.GET, httpEntity, String.class);
    }

    @Then("I get http status account {int}")
    public void iGetHttpStatusAccounts(int status) {
        Assert.assertEquals(responseEntity.getStatusCodeValue(), status);
    }

}
