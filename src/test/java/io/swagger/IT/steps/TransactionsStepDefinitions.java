package io.swagger.IT.steps;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.swagger.IT.BaseClassTesting;
import io.swagger.model.Transaction;
import org.junit.Assert;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

import java.net.URI;
import java.net.URISyntaxException;

public class TransactionsStepDefinitions extends BaseClassTesting {

    @When("I retrieve all transactions")
    public void iRetrieveAllTransactions() throws URISyntaxException {
        uri = new URI(baseUrl+"/Users");
        httpEntity = new HttpEntity<>(null, headers);
        responseEntity = template.exchange(uri, HttpMethod.GET, httpEntity, String.class);
    }

    @When("I retrieve all transactions with limit {int}, offset {int} and username {string}")
    public void iRetrieveAllTransactionsByUsername(int limit, int offset, String username) throws URISyntaxException {
        uri = new URI(baseUrl+"/Users");
        headers.add("limit", String.valueOf(limit));
        headers.add("offset", String.valueOf(offset));
        headers.add("username", username);
        httpEntity = new HttpEntity<>(null, headers);
        responseEntity = template.exchange(uri, HttpMethod.GET, httpEntity, String.class);
    }

    @When("I retrieve all transactions with limit {int}, offset {int} and dateFrom {string} and dateTo {string}")
    public void iRetrieveAllTransactionsByDateFromTo(int limit, int offset, String dateFrom, String dateTo) throws URISyntaxException {
        uri = new URI(baseUrl+"/Users");
        headers.add("limit", String.valueOf(limit));
        headers.add("offset", String.valueOf(offset));
        headers.add("dateFrom", dateFrom);
        headers.add("dateTo", dateTo);
        httpEntity = new HttpEntity<>(null, headers);
        responseEntity = template.exchange(uri, HttpMethod.GET, httpEntity, String.class);
    }

    @When("I create a transaction")
    public void iCreateTransaction() throws URISyntaxException {
        uri = new URI(baseUrl+"/Transactions");
        headers.setContentType(MediaType.APPLICATION_JSON);
        String transactions = "{\"accountFrom\": \"NL01INHO4996947694\", \"accountTo\": \"NL01INHO4995677694\", \"amount\": 10.0, \"description\": \"TestDescription\", \"userPerformingId\": 2, \"transactionType\": \"Deposit\"}";
        httpEntity = new HttpEntity<>(transactions, headers);
        responseEntity = template.exchange(uri, HttpMethod.POST, httpEntity, String.class);
    }

    @When("I retrieve transactions by accountId {int}")
    public void iRetrieveAllTransactionsByAccountId(int accountId) throws URISyntaxException {
        uri = new URI(baseUrl+"/Accounts/"+accountId+"/Transactions");
        httpEntity = new HttpEntity<>(null, headers);
        responseEntity = template.exchange(uri, HttpMethod.GET, httpEntity, String.class);
    }

    @When("I retrieve transactions by userId {int}")
    public void iRetrieveAllTransactionsByUserId(int userId) throws URISyntaxException {
        uri = new URI(baseUrl+"/Users/"+userId+"/Transactions");
        httpEntity = new HttpEntity<>(null, headers);
        responseEntity = template.exchange(uri, HttpMethod.GET, httpEntity, String.class);
    }


    @Then("I get http status transaction {int}")
    public void iGetHttpStatusTransactions(int status) {
        Assert.assertEquals(responseEntity.getStatusCodeValue(), status);
    }
}
