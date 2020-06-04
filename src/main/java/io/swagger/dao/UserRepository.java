package io.swagger.dao;

import io.swagger.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    // get user by Id
    User getUserByIdEquals(Long id);

    // get all users in the system
    List<User> findAll(Pageable pageable);

    // get all users that equals with the string of usernames
    List<User> getAllByUsernameContainingIgnoreCaseAndActiveIsFalse(String Username, Pageable pageable);

    // get all users that equals with the string of lastnames
    List<User> getAllByLastNameContainingIgnoreCaseAndActiveIsFalse(String lastname, Pageable pageable);

    User getUserByUsernameEquals(String username);
}
