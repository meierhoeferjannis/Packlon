package de.oth.packlon.service;

import de.oth.packlon.entity.Account;
import de.oth.packlon.entity.Delivery;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface IAccountService extends UserDetailsService {
    Account createAccount(Account account);

    Account addDelivery(long accountId, Delivery delivery);

    boolean existsAccountWithEmail(String email);

    @Override
    UserDetails loadUserByUsername(String email) throws UsernameNotFoundException;

    Account getAccountByEmail(String email);

    void deleteAccount(long accountId);

    Account updateAccount(Account account);
}
