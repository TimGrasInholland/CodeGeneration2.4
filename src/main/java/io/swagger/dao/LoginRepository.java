package io.swagger.dao;

import io.swagger.model.Body;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginRepository extends CrudRepository<Body, Long> {
}
