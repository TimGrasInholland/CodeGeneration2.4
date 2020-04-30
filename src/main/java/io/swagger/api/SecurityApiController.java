package io.swagger.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.*;
import io.swagger.model.Account;
import io.swagger.model.SessionToken;
import io.swagger.model.User;
import io.swagger.service.LoginService;
import io.swagger.service.SessionTokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;
import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Random;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-04-30T13:49:21.820Z[GMT]")
@Controller
public class SecurityApiController implements SecurityApi {

    private static final Logger log = LoggerFactory.getLogger(SecurityApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @Autowired
    private LoginService loginService;

    @Autowired
    private SessionTokenService sessionTokenService;

    @org.springframework.beans.factory.annotation.Autowired
    public SecurityApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    public ResponseEntity<String> logout() {
        String authKey = request.getHeader("session");
        try {
            if (sessionTokenService.isPermitted(authKey, User.TypeEnum.CUSTOMER)) {
                sessionTokenService.logout(authKey);
                return ResponseEntity.status(200).body("You are logged out");
            } else{
                return ResponseEntity.status(400).body("You are not logged in");
            }
        } catch (IllegalArgumentException e){
            return ResponseEntity.status(400).body("FCK");
        }
    }

    public ResponseEntity<String> login(@ApiParam(value = "") @RequestParam(value="username", required=false)  String username
            ,@ApiParam(value = "") @RequestParam(value="password", required=false)  String password
    ) {
        try {
            User user = loginService.login(username, password);
            if (user != null){
                String authKey = "";
                Random rnd = new Random();
                for (int i = 0; i < 10; i++) {
                    authKey += rnd.nextInt(10);
                }

                SessionToken sessionToken = new SessionToken(authKey, user.getId(), user.getType());
                sessionTokenService.registerSessionToken(sessionToken);

                return ResponseEntity.status(200).body(authKey);
            } else{
                return ResponseEntity.status(400).body("Invalid credentials");
            }
        } catch (IllegalArgumentException e){
            return ResponseEntity.status(400).body("You are already logged in");
        }

    }

}
