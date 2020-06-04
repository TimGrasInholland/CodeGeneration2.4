package io.swagger.dao;


import io.swagger.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginRepository extends CrudRepository<User, Long> {

    User getUserByUsernameEqualsAndPasswordEqualsAndActiveIsTrue(String username, String password);

}
