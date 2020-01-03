package de.oth.Packlon.controller;

import de.oth.Packlon.entity.Account;
import de.oth.Packlon.entity.Address;
import de.oth.Packlon.entity.Customer;
import de.oth.Packlon.service.AccountService;
import de.oth.Packlon.service.AddressService;
import de.oth.Packlon.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class RegistrationController {

    @Autowired
    private AccountService accountService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private AddressService addressService;

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
     /*   if (firstName.length() < 3 || firstName.trim().isEmpty()) {
            model.addAttribute(" firstNameErrorMessage", "Firstname has to be longer than 3 letters");
            valid = false;
        }

        if (lastName.length() < 3 || lastName.trim().isEmpty()) {
            valid = false;
            model.addAttribute("lastNameErrorMessage", "Lastname has to be longer than 3 letters");
        }
        */

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
        account.addAddress(account.getHomeAddress());
        account.setOwner(customer);
        if (valid) {
            Account nAcc = new Account();
            model.addAttribute("account", nAcc);
            accountService.createAccount(account);
        }

        return "login";
    }
}
