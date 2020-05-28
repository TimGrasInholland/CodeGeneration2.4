package io.swagger.api;

import io.swagger.dao.SessionTokenRepository;
import io.swagger.model.Account;
import io.swagger.model.SessionToken;
import io.swagger.model.User;
import io.swagger.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class Security {

    @Autowired
    private SessionTokenRepository sessionTokenRepository;

    @Autowired
    private UserService userService;

    public boolean isPermitted(String authKey, User.TypeEnum requiredRole){
        if (authKey != null){
            SessionToken sessionToken = sessionTokenRepository.getByAuthKeyEquals(authKey);
            if (sessionToken != null){
                return requiredRole.equals(sessionToken.getRole()) || sessionToken.getRole().equals(User.TypeEnum.EMPLOYEE);
            }
            return false;
        }
        return false;
    }

    public boolean isPermittedAndNotBank(String authKey, User.TypeEnum requiredRole, User.TypeEnum requestedRole){
        if (isPermitted(authKey, requiredRole)){
            if (!bankCheck(requestedRole)){
                return true;
            }
            return false;
        }
        return false;
    }

    public boolean isOwnerOrPermitted(String authKey, User.TypeEnum requiredRole, Long userId){
        if (isOwner(authKey, userId)){
            return true;
        } else if (isPermitted(authKey, requiredRole)){
            return true;
        } else{
            return false;
        }
    }

    public boolean isOwnerOrEmployee(String authKey, Long userId){
        if (authKey != null){
            if (isOwner(authKey, userId)){
                return true;
            } else if (employeeCheck(authKey)){
                return true;
            } else{
                return false;
            }
        }
        return false;
    }

    public boolean employeeCheck(String authKey){
        SessionToken sessionToken = sessionTokenRepository.getByAuthKeyEquals(authKey);
        return sessionToken.getRole().equals(User.TypeEnum.EMPLOYEE);
    }

    public boolean bankCheck(User.TypeEnum type){
        return type.equals(User.TypeEnum.BANK);
    }

    public boolean customerCheck(String authKey){
        SessionToken sessionToken = sessionTokenRepository.getByAuthKeyEquals(authKey);
        return sessionToken.getRole().equals(User.TypeEnum.CUSTOMER);
    }

    public boolean isOwner(String authKey, Long userId){
        SessionToken sessionToken = sessionTokenRepository.getByAuthKeyEquals(authKey);
        return sessionToken.getUserId().equals(userId);
    }

    public List<User> filterUsers(List<User> users){
        List<User> cleanedUserList = new ArrayList<>();

        for (User user : users) {
            if (!user.getType().equals(User.TypeEnum.BANK)){
                cleanedUserList.add(user);
            }
        }
        return cleanedUserList;
    }

    public List<Account> filterAccounts(List<Account> accounts){
        List<Account> cleanedAccountList = new ArrayList<>();

        for (Account account : accounts) {
            if (!userService.getUserById(account.getUserId()).getType().equals(User.TypeEnum.BANK)){
                cleanedAccountList.add(account);
            }
        }
        return cleanedAccountList;
    }

}
