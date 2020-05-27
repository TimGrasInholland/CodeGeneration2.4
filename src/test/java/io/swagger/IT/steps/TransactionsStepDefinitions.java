package io.swagger.IT.steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.core.internal.gherkin.deps.com.google.gson.Gson;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.swagger.model.Transaction;
import org.junit.Assert;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.threeten.bp.OffsetDateTime;

import java.net.URI;
import java.net.URISyntaxException;

public class TransactionsStepDefinitions {

    HttpHeaders headers = new HttpHeaders();
    RestTemplate template = new RestTemplate();
    ObjectMapper mapper = new ObjectMapper();
    ResponseEntity<String> responseEntity;
    String baseUrl = "http://localhost:8080/api/Transactions";
    URI uri;

    public TransactionsStepDefinitions() throws URISyntaxException {
        this.headers.add("session","testEmployee");
        this.uri = new URI(baseUrl);
    }

    @When("I retrieve all transactions")
    public void iRetrieveAllTransactions() {
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        responseEntity = template.exchange(uri, HttpMethod.GET, entity, String.class);
    }

    @When("I retrieve all transactions with limit {int}, offset {int} and username {string}")
    public void iRetrieveAllTransactionsByUsername(int limit, int offset, String username) {
        headers.add("limit", String.valueOf(limit));
        headers.add("offset", String.valueOf(offset));
        headers.add("username", username);
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        responseEntity = template.exchange(uri, HttpMethod.GET, entity, String.class);
    }

    @When("I retrieve all transactions with limit {int}, offset {int} and dateFrom {string} and dateTo {string}")
    public void iRetrieveAllTransactionsByDateFromTo(int limit, int offset, String dateFrom, String dateTo) {
        headers.add("limit", String.valueOf(limit));
        headers.add("offset", String.valueOf(offset));
        headers.add("dateFrom", dateFrom);
        headers.add("dateTo", dateTo);
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        responseEntity = template.exchange(uri, HttpMethod.GET, entity, String.class);
    }

    @When("I create a transaction")
    public void iCreateTransaction() throws JsonProcessingException {
        Transaction transaction = new Transaction("NL01INHO6666934694", "NL01INHO4996947694", 10.0, "TestDescription", 2L, Transaction.TransactionTypeEnum.DEPOSIT);
        headers.setContentType(MediaType.APPLICATION_JSON);
        Gson gson = new Gson();
        gson.toJson(transaction);
        String transactionS = "{\"accountFrom\": \"NL01INHO6666934694\", \"accountTo\": \"NL01INHO6666134694\", \"amount\": 10.0, \"description\": \"TestDescription\", \"userPerformingId\": 2, \"transactionType\": \"Deposit\"}";
        HttpEntity<String> entity = new HttpEntity<>(transactionS, headers);
        responseEntity = template.exchange(uri, HttpMethod.POST, entity, String.class);
    }

    @When("I retrieve transactions by accountId {int}")
    public void iRetrieveAllTransactionsByAccountId(int accountId) throws URISyntaxException {
        URI uri = new URI("http://localhost:8080/api/Accounts/"+accountId+"/Transactions");
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        responseEntity = template.exchange(uri, HttpMethod.GET, entity, String.class);
    }

    @When("I retrieve transactions by userId {int}")
    public void iRetrieveAllTransactionsByUserId(int userId) throws URISyntaxException {
        URI uri = new URI("http://localhost:8080/api/Users/"+userId+"/Transactions");
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        responseEntity = template.exchange(uri, HttpMethod.GET, entity, String.class);
    }


    @Then("I get http status transaction {int}")
    public void iGetHttpStatusTransactions(int status) {
        Assert.assertEquals(responseEntity.getStatusCodeValue(), status);
    }
}
