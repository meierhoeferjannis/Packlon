package de.oth.packlon.repository;


import de.oth.packlon.entity.Account;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AccountRepository  extends CrudRepository<Account, Long> {
    public boolean existsAccountByEmail(String email);
    public Optional<Account> findByEmail(String email);
}
