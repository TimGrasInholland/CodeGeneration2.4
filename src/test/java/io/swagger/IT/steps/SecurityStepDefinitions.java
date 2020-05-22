package io.swagger.IT.steps;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

public class SecurityStepDefinitions {

    HttpHeaders headers = new HttpHeaders();
    RestTemplate template = new RestTemplate();
    ObjectMapper mapper = new ObjectMapper();
    ResponseEntity<String> responseEntity;
    String baseUrl = "http://localhost:8080/api/";
    URI uri;

    public SecurityStepDefinitions() throws URISyntaxException {
        this.headers.add("session","testEmployee");
        this.uri = new URI(baseUrl);
    }

    @When("I login with username {string} and password {string}")
    public void iLogin(String username, String password) throws URISyntaxException {
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> body = new LinkedMultiValueMap();
        body.add("username", username);
        body.add("password", password);
        HttpEntity<String> entity = new HttpEntity(body, headers);
        URI uriLogin = new URI(baseUrl+"Login");
        responseEntity = template.exchange(uriLogin, HttpMethod.POST, entity, String.class);
    }

    @Then("I get http status security {int}")
    public void iGetHttpStatusSecurity(int status) {
        Assert.assertEquals(responseEntity.getStatusCodeValue(), status);
    }
}
