package io.swagger.service;

import io.swagger.dao.SessionTokenRepository;
import io.swagger.model.SessionToken;
import io.swagger.model.User;
import org.springframework.stereotype.Service;

@Service
public class SessionTokenService {

    private SessionTokenRepository sessionTokenRepository;

    public SessionTokenService(SessionTokenRepository sessionTokenRepository) {
        this.sessionTokenRepository = sessionTokenRepository;
    }

    public void registerSessionToken(SessionToken sessionToken){
        SessionToken checkSessionToken = sessionTokenRepository.getByUserIdEquals(sessionToken.getUserId());
        if (checkSessionToken != null){
            sessionTokenRepository.deleteByUserIdEquals(sessionToken.getUserId());
            sessionTokenRepository.save(sessionToken);
        } else{
            sessionTokenRepository.save(sessionToken);
        }
    }

    public boolean isPermitted(String authKey, User.TypeEnum role){
        SessionToken sessionToken = new SessionToken();
        if (!authKey.equals(null)){
            sessionToken = sessionTokenRepository.getByAuthKeyEquals(authKey);
        }

        if (sessionToken != null && role.equals(sessionToken.getRole())){
            return true;
        }
        return false;
    }

    public void logout(String authKey){
        sessionTokenRepository.deleteByAuthKeyEquals(authKey);
    }



}
