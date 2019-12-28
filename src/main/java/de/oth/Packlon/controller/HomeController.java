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


    @RequestMapping("/")
    public String starten() {

        return "index";
    }
    @RequestMapping("/index")
    public String index(){
        return "index";
    }


}
