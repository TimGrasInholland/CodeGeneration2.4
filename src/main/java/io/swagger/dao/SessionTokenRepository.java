package io.swagger.dao;


import io.swagger.model.SessionToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionTokenRepository extends CrudRepository<SessionToken, Long> {

    SessionToken getByAuthKeyEquals(String authKey);

    SessionToken getByUserIdEquals(Long id);

}
