package io.swagger.dao;

import io.swagger.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

    @Query("DELETE FROM User u WHERE u.id = ?1 ")
    void deleteByIdEquals(Integer id);

    User getUserByIdEquals(Integer id);
}
