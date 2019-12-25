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
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private AccountRoleRepository accountRoleRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

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
       return  accountRepository.save(ret);

    }

    public Account addDeliverys(long accountId, List<Delivery> deliveryList) {
        Optional<Account> account = accountRepository.findById(accountId);
        account.ifPresent(acc -> {
            for (Delivery delivery : deliveryList) {
                acc.addDelivery(delivery);
            }
            ;
            accountRepository.save(acc);
        });

        return account.get();
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

    public void deleteAccount(long accountId) {
        accountRepository.deleteById(accountId);
    }

}
