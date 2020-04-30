package io.swagger.dao;


import io.swagger.model.SessionToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionTokenRepository extends CrudRepository<SessionToken, String> {

    public SessionToken getByAuthKeyEquals(String authKey);

    public SessionToken getByUserIdEquals(Long id);

    public void deleteByAuthKeyEquals(String authKey);

    public void deleteByUserIdEquals(Long userId);
}
