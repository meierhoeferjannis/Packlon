package de.oth.packlon.service;


import de.oth.packlon.entity.*;
import de.oth.packlon.repository.AccountRepository;
import de.oth.packlon.repository.AccountRoleRepository;
import de.oth.packlon.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Qualifier("accountService")
public class AccountService implements IAccountService {
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

    @Override
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
    @Override
    public Account addDelivery(long accountId, Delivery delivery){
      Account  account = accountRepository.findById(accountId).get();
      account.addDelivery(delivery);
      accountRepository.save(account);
      return account;
    }

    @Override
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

    @Override
    public Account getAccountByEmail(String email) {
       return accountRepository.findByEmail(email).get();
    }

    @Override
    public void deleteAccount(long accountId) {
        accountRepository.deleteById(accountId);
    }
    @Override
    public Account updateAccount(Account account){
        return accountRepository.save(account);
    }

}
