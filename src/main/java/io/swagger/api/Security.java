package io.swagger.api;

import io.swagger.model.Account;
import io.swagger.model.SessionToken;
import io.swagger.model.User;
import io.swagger.service.SessionTokenService;
import io.swagger.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class Security {

    @Autowired
    private SessionTokenService sessionTokenService;

    @Autowired
    private UserService userService;

    // Check if the user has the required role by getting his role with his authKey.
    public boolean isPermitted(String authKey, User.TypeEnum requiredRole){
        if (authKey != null){
            SessionToken sessionToken = sessionTokenService.getSessionTokenByAuthKey(authKey);
            if (sessionToken != null){
                return requiredRole.equals(sessionToken.getRole()) || sessionToken.getRole().equals(User.TypeEnum.EMPLOYEE);
            }
            return false;
        }
        return false;
    }

    // Check if the user is permitted based on authKey and check to make sure role is not BANK.
    public boolean isPermittedAndNotBank(String authKey, User.TypeEnum requiredRole, User.TypeEnum requestedRole){
        if (isPermitted(authKey, requiredRole)){
            if (!bankCheck(requestedRole)){
                return true;
            }
            return false;
        }
        return false;
    }

    // Check if the user is the owner of a given account or userAccount or is permitted to change and/ or update files which are not his own.
    public boolean isOwnerOrPermitted(String authKey, User.TypeEnum requiredRole, Long userId){
        if (isOwner(authKey, userId)){
            return true;
        } else if (isPermitted(authKey, requiredRole)){
            return true;
        } else{
            return false;
        }
    }

    // Check if the user is owner or Employee.
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

    // Check if the user is employee.
    public boolean employeeCheck(String authKey){
        SessionToken sessionToken = sessionTokenService.getSessionTokenByAuthKey(authKey);
        return sessionToken.getRole().equals(User.TypeEnum.EMPLOYEE);
    }

    // Check if given type is BANK
    public boolean bankCheck(User.TypeEnum type){
        return type.equals(User.TypeEnum.BANK);
    }

    // CHeck if given authKey belongs to a customer
    public boolean customerCheck(String authKey){
        SessionToken sessionToken = sessionTokenService.getSessionTokenByAuthKey(authKey);
        return sessionToken.getRole().equals(User.TypeEnum.CUSTOMER);
    }

    // Check if given authKey belongs to the user which is being updated or changed.
    public boolean isOwner(String authKey, Long userId){
        SessionToken sessionToken = sessionTokenService.getSessionTokenByAuthKey(authKey);
        return sessionToken.getUserId().equals(userId);
    }

    // Check if the list of users contains the BANK account.
    public List<User> filterUsers(List<User> users){
        List<User> cleanedUserList = new ArrayList<>();

        for (User user : users) {
            if (!user.getType().equals(User.TypeEnum.BANK)){
                cleanedUserList.add(user);
            }
        }
        return cleanedUserList;
    }

    // Check if the given accounts list contains a BANK account.
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
