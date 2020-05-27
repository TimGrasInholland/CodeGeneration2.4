package io.swagger.IT.steps;

import com.fasterxml.jackson.databind.ObjectMapper;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.swagger.model.SessionToken;
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

    @When("I logout")
    public void iLogout() throws URISyntaxException {
        headers.set("session", "0");
        headers.setContentType(MediaType.APPLICATION_JSON);
        URI uriLogin = new URI(baseUrl+"Logout");
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        responseEntity = template.exchange(uriLogin, HttpMethod.DELETE, entity, String.class);
    }

    @When("I retrieve a sessionToken by authKey is {string}")
    public SessionToken iGetSessionTokenByAuthKey(String authKey) throws URISyntaxException {
        URI uri = new URI(baseUrl+"/SessionToken/"+authKey);
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        responseEntity = template.exchange(uri, HttpMethod.GET, entity, String.class);
        return template.exchange(uri, HttpMethod.GET, entity, SessionToken.class).getBody();
    }

    @Then("I get http status security {int}")
    public void iGetHttpStatusSecurity(int status) {
        Assert.assertEquals(responseEntity.getStatusCodeValue(), status);
    }
}
