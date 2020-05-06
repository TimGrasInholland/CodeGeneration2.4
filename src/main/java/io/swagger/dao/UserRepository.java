package io.swagger.dao;

import io.swagger.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    User getUserByIdEquals(Long id);

    List<User> findAll(Pageable pageable);

    List<User> getAllByUsernameContainingIgnoreCase(String Username, Pageable pageable);

    List<User> getAllByLastNameContainingIgnoreCase(String lastname, Pageable pageable);

    User getUserByUsernameEquals(String username);
}
