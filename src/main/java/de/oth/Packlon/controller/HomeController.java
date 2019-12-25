package de.oth.Packlon.controller;


import de.oth.Packlon.entity.Account;
import de.oth.Packlon.entity.Customer;
import de.oth.Packlon.service.AccountService;
import de.oth.Packlon.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class HomeController {


    @Autowired
    private AccountService accountService;
    @Autowired
    private CustomerService customerService;

    @RequestMapping("/")
    public String starten(Model model) {
        Account account = new Account();
        model.addAttribute("account", account);
        return "index";
    }

    @RequestMapping(value = "/register", method =  RequestMethod.POST)
    public String register(
            Account account,
            @ModelAttribute("firstName") String firstName,
            @ModelAttribute("lastName") String lastName,
            Model model
    ) {
        boolean valid = true;

        if (accountService.existsAccountWithEmail(account.getEmail())) {
            valid = false;
        }
        if (firstName.length() < 3 || firstName.trim().isEmpty()) {
            model.addAttribute(" firstNameErrorMessage", "Firstname has to be longer than 3 letters");
            valid = false;
        }

        if (lastName.length() < 3 || lastName.trim().isEmpty()) {
            valid = false;
            model.addAttribute("lastNameErrorMessage", "Lastname has to be longer than 3 letters");
        }
        if (!valid)
            return "index";


        Customer customer = new Customer();
        customer.setLastName(lastName);
        customer.setFirstName(firstName);
        customer = customerService.getCustomerByName(customer);
        account.setOwner(customer);


        if (valid) {
            Account nAcc = new Account();
            model.addAttribute("account", nAcc);
            accountService.createAccount(account);
        }

        return "login";

    }

    @RequestMapping(value = {"/login"}, method = RequestMethod.GET)
    public String login() {
        return "login";
    }


}
