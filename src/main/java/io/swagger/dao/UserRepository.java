package io.swagger.dao;

import io.swagger.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    public List<User> findAll(Pageable pageable);
    public List<User> getAllByUsernameContainingIgnoreCase(String Username, Pageable pageable);
    public List<User> getAllByLastNameContainingIgnoreCase(String lastname, Pageable pageable);
}
