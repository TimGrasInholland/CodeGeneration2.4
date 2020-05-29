package io.swagger.service;

import io.swagger.dao.SessionTokenRepository;
import io.swagger.model.SessionToken;
import org.springframework.stereotype.Service;

@Service
public class SessionTokenService {

    private final SessionTokenRepository sessionTokenRepository;

    public SessionTokenService(SessionTokenRepository sessionTokenRepository) {
        this.sessionTokenRepository = sessionTokenRepository;
    }

    // Log the user in by registering the sessionToken.
    public void registerSessionToken(SessionToken sessionToken) {
        SessionToken checkSessionToken = sessionTokenRepository.getByUserIdEquals(sessionToken.getUserId());
        // If the session token of this user already exists he is already logged in so we delete the old token and save the new.
        // By doing this the user can only be logged in on one device.
        if (checkSessionToken != null) {
            sessionTokenRepository.delete(checkSessionToken);
            sessionTokenRepository.save(sessionToken);
        } else {
            sessionTokenRepository.save(sessionToken);
        }
    }

    public SessionToken getSessionTokenByUserIdEquals(Long userId){
        return sessionTokenRepository.getByUserIdEquals(userId);
    }

    public SessionToken getSessionTokenByAuthKey(String authKey) {
        return sessionTokenRepository.getByAuthKeyEquals(authKey);
    }

    public void logout(String authKey) {
        SessionToken sessionToken = getSessionTokenByAuthKey(authKey);
        sessionTokenRepository.delete(sessionToken);
    }


}
