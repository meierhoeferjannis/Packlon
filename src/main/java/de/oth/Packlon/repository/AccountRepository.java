package de.oth.Packlon.repository;


import de.oth.Packlon.entity.Account;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AccountRepository  extends CrudRepository<Account, Long> {
    public boolean existsAccountByEmail(String email);
    public Optional<Account> findByEmail(String email);
}
