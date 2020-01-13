package de.oth.Packlon.service;


import de.oth.Packlon.entity.*;
import de.oth.Packlon.repository.AccountRepository;
import de.oth.Packlon.repository.AccountRoleRepository;
import de.oth.Packlon.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Qualifier("accountService")
public class AccountService implements UserDetailsService {
    private final AccountRepository accountRepository;
    private final AccountRoleRepository accountRoleRepository;
    private final RoleRepository roleRepository;
    private final CustomerService customerService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public AccountService(AccountRepository accountRepository, AccountRoleRepository accountRoleRepository, RoleRepository roleRepository, CustomerService customerService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.accountRepository = accountRepository;
        this.accountRoleRepository = accountRoleRepository;
        this.roleRepository = roleRepository;
        this.customerService = customerService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public Account createAccount(Account account) {

        Customer customer = customerService.getCustomerByName(account.getOwner());
        account.setOwner(customer);
        account.setPassword(bCryptPasswordEncoder.encode(account.getPassword()));
        Account ret = accountRepository.save(account);
        Role role = roleRepository.findByRole("USER");
        AccountRole accountRole = new AccountRole();
        accountRole.setRole(role);
        accountRole.setAccount(ret);
        ret.addAccountRole(accountRole);
        return accountRepository.save(ret);

    }
    public Account addDelivery(long accountId, Delivery delivery){
      Account  account = accountRepository.findById(accountId).get();
      account.addDelivery(delivery);
      accountRepository.save(account);
      return account;
    }

    public boolean existsAccountWithEmail(String email) {
        return accountRepository.existsAccountByEmail(email);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> {
                            throw new UsernameNotFoundException("Account mit email " + email + " existiert nicht");
                        }
                );
        return account;

    }

    public Account getAccountByEmail(String email) {
       return accountRepository.findByEmail(email).get();
    }

    public void deleteAccount(long accountId) {
        accountRepository.deleteById(accountId);
    }
    public Account updateAccount(Account account){
        return accountRepository.save(account);
    }

}
