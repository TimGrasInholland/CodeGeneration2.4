package io.swagger.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiParam;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Min;
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

    @Autowired
    private Security security;

    @org.springframework.beans.factory.annotation.Autowired
    public SecurityApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    public ResponseEntity<String> logout() {
        String authKey = request.getHeader("session");
        try {
            if (security.isPermitted(authKey, User.TypeEnum.CUSTOMER)) {
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


    public ResponseEntity<SessionToken> getSessionTokenByAuthKey(@Min(1)@ApiParam(value = "",required=true, allowableValues="") @PathVariable("APIkey") String key) {
        if (key != null && security.isPermitted(key, User.TypeEnum.CUSTOMER)) {
            try {
                return ResponseEntity.status(200).body(sessionTokenService.getSessionTokenByAuthKey(key));
            } catch (IllegalArgumentException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<SessionToken>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return new ResponseEntity<SessionToken>(HttpStatus.UNAUTHORIZED);
    }

}
