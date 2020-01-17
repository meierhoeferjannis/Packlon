package de.oth.packlon.controller;

import de.oth.packlon.entity.Account;
import de.oth.packlon.entity.Customer;
import de.oth.packlon.service.AccountService;
import de.oth.packlon.service.AddressService;
import de.oth.packlon.service.CustomerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class RegistrationController {

    private final AccountService accountService;
    private final CustomerService customerService;
    private final AddressService addressService;

    public RegistrationController(AccountService accountService, CustomerService customerService, AddressService addressService) {
        this.accountService = accountService;
        this.customerService = customerService;
        this.addressService = addressService;
    }

    @RequestMapping(value = "/registration", method = RequestMethod.GET)
    public String registration(
            Model model
    ) {
        Account account = new Account();
        model.addAttribute("account", account);
        return "registration";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register(
            Account account,

            Model model
    ) {
        boolean valid = true;

        if (accountService.existsAccountWithEmail(account.getEmail())) {
            valid = false;
        }
           if (!valid)
            return "index";

        Customer customer = new Customer();
        customer.setLastName(account.getOwner().getLastName());
        customer.setFirstName(account.getOwner().getFirstName());
        customer = customerService.getCustomerByName(customer);

        if (!addressService.existsAddress(account.getHomeAddress())) {
            account.setHomeAddress(addressService.createAddress(account.getHomeAddress()));
        } else {
            account.setHomeAddress(addressService.getAddress(account.getHomeAddress()));
        }

        account.setOwner(customer);
        if (valid) {
            Account nAcc = new Account();
            model.addAttribute("account", nAcc);
            accountService.createAccount(account);
        }

        return "login";
    }
}
