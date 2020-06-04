package io.swagger.dao;

import io.swagger.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    // get user by Id
    User getUserByIdEqualsAndActiveIsTrue(Long id);

    // get all users in the system
    List<User> getAllByActiveIsTrueAndTypeIsNot(Pageable pageable, User.TypeEnum type);

    // get all users that equals with the string of usernames
    List<User> getAllByUsernameContainingIgnoreCaseAndActiveIsTrueAndTypeIsNot(String Username, Pageable pageable, User.TypeEnum type);

    // get all users that equals with the string of lastnames
    List<User> getAllByLastNameContainingIgnoreCaseAndActiveIsTrueAndTypeIsNot(String lastname, Pageable pageable, User.TypeEnum type);

    User getUserByUsernameEqualsAndActiveIsTrue(String username);
}
