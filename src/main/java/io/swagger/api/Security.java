package io.swagger.api;

import io.swagger.dao.SessionTokenRepository;
import io.swagger.model.SessionToken;
import io.swagger.model.User;
import org.apache.catalina.SessionEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Security {

    @Autowired
    private SessionTokenRepository sessionTokenRepository;

    public boolean isPermitted(String authKey, User.TypeEnum requiredRole){
        SessionToken sessionToken = sessionTokenRepository.getByAuthKeyEquals(authKey);
        if (sessionToken != null){
            if (requiredRole.equals(sessionToken.getRole()) || sessionToken.getRole().equals(User.TypeEnum.EMPLOYEE)){
                return true;
            }
        }
        return false;
    }

    public boolean isOwner(String authKey, Long userId){
        SessionToken sessionToken = sessionTokenRepository.getByAuthKeyEquals(authKey);
        if (sessionToken.getUserId().equals(userId)){
            return true;
        } else {
            return false;
        }
    }
}
