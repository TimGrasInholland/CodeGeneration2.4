package io.swagger.IT.steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.swagger.IT.BaseClassTesting;
import io.swagger.model.User;
import net.minidev.json.JSONObject;
import org.junit.Assert;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

import java.net.URI;
import java.net.URISyntaxException;

public class UsersStepDefinitions extends BaseClassTesting {

    @When("I retrieve all users")
    public void iRetrieveAllUsers() throws URISyntaxException {
        uri = new URI(baseUrl + "/Users");
        httpEntity = new HttpEntity<>(null, headers);
        responseEntity = template.exchange(uri, HttpMethod.GET, httpEntity, String.class);
    }

    @When("I retrieve user by id {int}")
    public void iGetUserById(Integer id) throws URISyntaxException {
        URI uri = new URI(baseUrl + "/Users/" + id);
        httpEntity = new HttpEntity<>(null, headers);
        responseEntity = template.exchange(uri, HttpMethod.GET, httpEntity, String.class);
    }

    @When("I create an user")
    public void iCreateUser() throws JsonProcessingException, URISyntaxException {
        uri = new URI(baseUrl + "/Users");
        User user = new User("test1234", "Welkom567!", "test", "", "Tester", "test@test.nl", "2002-05-05", "Lepellaan 2", "1544MK", "Haarlem", "0611111111", User.TypeEnum.CUSTOMER, true);
        headers.setContentType(MediaType.APPLICATION_JSON);
        httpEntity = new HttpEntity<>(mapper.writeValueAsString(user), headers);
        responseEntity = template.exchange(uri, HttpMethod.POST, httpEntity, String.class);
    }

    @When("I update an user")
    public void iUpdateUser() throws JsonProcessingException, URISyntaxException {
        uri = new URI(baseUrl + "/Users");
        JSONObject obj = new JSONObject();
        obj.put("id", 2);
        obj.put("username", "Adrie5388");
        obj.put("password", "Welkom123!");
        obj.put("firstName", "Andries");
        obj.put("prefix", "");
        obj.put("lastName", "Komen");
        obj.put("email", "AndriesK@gmail.com");
        obj.put("birthdate", "1992-11-03");
        obj.put("address", "Bloemendotter 12");
        obj.put("postalcode", "1958TX");
        obj.put("city", "Haarlem");
        obj.put("phoneNumber", "0637291827");
        obj.put("type", User.TypeEnum.EMPLOYEE);
        obj.put("active", true);
        headers.setContentType(MediaType.APPLICATION_JSON);
        httpEntity = new HttpEntity<>(mapper.writeValueAsString(obj), headers);
        responseEntity = template.exchange(uri, HttpMethod.PUT, httpEntity, String.class);
    }

    @When("I retrieve an user by lastname {string}")
    public void GetUsersByLastname(String lastname) throws URISyntaxException {
        uri = new URI(baseUrl + "/Accounts");
        String s = "{\"offset\": 0,\n" +
                "\"limit\": 100,\n" +
                "\"searchname\": " + lastname + " }";
        httpEntity = new HttpEntity<>(s, headers);
        responseEntity = template.exchange(uri, HttpMethod.GET, httpEntity, String.class);
    }

    @When("I retrieve an user by username {string}")
    public void GetUsersByUsername(String username) throws URISyntaxException {
        uri = new URI(baseUrl + "/Users");
        String s = "{\"offset\": 0,\n" +
                "\"limit\": 100,\n" +
                "\"searchname\": " + username + " }";
        httpEntity = new HttpEntity<>(s, headers);
        responseEntity = template.exchange(uri, HttpMethod.GET, httpEntity, String.class);
    }

    @Then("I get http status users {int}")
    public void iGetHttpStatusUsers(int status) {
        Assert.assertEquals(responseEntity.getStatusCodeValue(), status);
    }
}