package io.swagger.IT.steps;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.swagger.IT.BaseClassTesting;
import io.swagger.model.SessionToken;
import org.junit.Assert;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.net.URI;
import java.net.URISyntaxException;

public class SecurityStepDefinitions extends BaseClassTesting {

    @When("I login with username {string} and password {string}")
    public void iLogin(String username, String password) throws URISyntaxException {
        URI uriLogin = new URI(baseUrl+"/Login");
        MultiValueMap<String, String> body = new LinkedMultiValueMap();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        body.add("username", username);
        body.add("password", password);
        httpEntity = new HttpEntity(body, headers);
        responseEntity = template.exchange(uriLogin, HttpMethod.POST, httpEntity, String.class);
    }

    @When("I logout with authKey is {string}")
    public void iLogout(String authKey) throws URISyntaxException {
        uri = new URI(baseUrl+"/Logout");
        headers.set("session", authKey);
        headers.setContentType(MediaType.APPLICATION_JSON);
        httpEntity = new HttpEntity<>(null, headers);
        responseEntity = template.exchange(uri, HttpMethod.DELETE, httpEntity, String.class);
    }

    @When("I retrieve a sessionToken by authKey is {string}")
    public SessionToken iGetSessionTokenByAuthKey(String authKey) throws URISyntaxException {
        URI uri = new URI(baseUrl+"/SessionToken/"+authKey);
        httpEntity = new HttpEntity<>(null, headers);
        responseEntity = template.exchange(uri, HttpMethod.GET, httpEntity, String.class);
        return template.exchange(uri, HttpMethod.GET, httpEntity, SessionToken.class).getBody();
    }

    @Then("I get http status security {int}")
    public void iGetHttpStatusSecurity(int status) {
        Assert.assertEquals(responseEntity.getStatusCodeValue(), status);
    }
}
