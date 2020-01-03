package de.oth.Packlon.controller;

import de.oth.Packlon.entity.Account;
import de.oth.Packlon.entity.Delivery;
import de.oth.Packlon.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Controller
public class AccountController {
    @Autowired
    private AccountService accountService;

    @RequestMapping(value = {"/account"}, method = RequestMethod.GET)
    public String account(Model model) {
        List<Delivery> unpaiedDeliverys = new ArrayList<Delivery>();
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            Account account = accountService.getAccountByEmail(auth.getName());
            unpaiedDeliverys = account.getDeliveryList().stream().filter(delivery -> delivery.getPaied() == false).collect(Collectors.toList());
            model.addAttribute("unpaiedDeliverys", unpaiedDeliverys);
        } catch (Exception e) {
            model.addAttribute("unpaiedDeliverys", new ArrayList<Delivery>());
        }


        return "account";
    }

    @RequestMapping
    public String cancelDelivery(Model model,
                                 @ModelAttribute("delivery") Delivery deliveryToDelete) {
        System.out.println(deliveryToDelete.getId());
        return "account";
    }
}
