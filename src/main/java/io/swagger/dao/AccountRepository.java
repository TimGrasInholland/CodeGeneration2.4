package io.swagger.dao;

import io.swagger.model.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends PagingAndSortingRepository<Account, Long> {

    Iterable<Account> findAccountsByUserIdAndActiveIsTrue(Long userId);

    Account findAccountByIbanEqualsAndActiveIsTrue(String iban);
    
    Integer countAccountByIbanEquals(String iban);

    // hoe werkt dit met pageAble? Die krijg ie namelijk niet mee van de service... naam aanpassen vind ie dus ook niet leuk.
    Page<Account> findAll(Pageable pageable);

    Account findAccountByIdAndActiveIsTrue(Long userId);
}
