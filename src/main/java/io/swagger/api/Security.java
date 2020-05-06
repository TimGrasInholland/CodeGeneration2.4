package io.swagger.api;

import io.swagger.dao.SessionTokenRepository;
import io.swagger.model.SessionToken;
import io.swagger.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Security {

    @Autowired
    private SessionTokenRepository sessionTokenRepository;

    public boolean isPermitted(String authKey, User.TypeEnum requiredRole){
        SessionToken sessionToken = sessionTokenRepository.getByAuthKeyEquals(authKey);
        if (sessionToken != null){
            return requiredRole.equals(sessionToken.getRole()) || sessionToken.getRole().equals(User.TypeEnum.EMPLOYEE);
        }
        return false;
    }

    public boolean employeeCheck(String authKey){
        SessionToken sessionToken = sessionTokenRepository.getByAuthKeyEquals(authKey);
        return sessionToken.getRole().equals(User.TypeEnum.EMPLOYEE);
    }

    public boolean customerCheck(String authKey){
        SessionToken sessionToken = sessionTokenRepository.getByAuthKeyEquals(authKey);
        return sessionToken.getRole().equals(User.TypeEnum.CUSTOMER);
    }

    public boolean isOwner(String authKey, Long userId){
        SessionToken sessionToken = sessionTokenRepository.getByAuthKeyEquals(authKey);
        return sessionToken.getUserId().equals(userId);
    }
}
