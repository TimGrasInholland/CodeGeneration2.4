package io.swagger.dao;


import io.swagger.model.SessionToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionTokenRepository extends CrudRepository<SessionToken, Long> {

    public SessionToken getByAuthKeyEquals(String authKey);

    public SessionToken getByUserIdEquals(Long id);

    public void deleteByAuthKeyEquals(String authKey);
}
