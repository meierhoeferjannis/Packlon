package de.oth.Packlon.controller;

import de.oth.Packlon.entity.Account;
import de.oth.Packlon.entity.Delivery;
import de.oth.Packlon.service.AccountService;
import de.oth.Packlon.service.DeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Controller
public class AccountController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private DeliveryService deliveryService;

    @RequestMapping(value = {"/account"}, method = RequestMethod.GET)
    public String account(Model model) {

        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            Account account = accountService.getAccountByEmail(auth.getName());
            model.addAttribute("account",account);
            model.addAttribute("paiedDeliverys",account.getDeliveryList().stream().filter(delivery -> delivery.getPaied() == true).collect(Collectors.toList()));
            model.addAttribute("unpaiedDeliverys",  account.getDeliveryList().stream().filter(delivery -> delivery.getPaied() == false).collect(Collectors.toList()));
        } catch (Exception e) {
            model.addAttribute("unpaiedDeliverys", new ArrayList<Delivery>());
            model.addAttribute("paiedDeliverys", new ArrayList<Delivery>());
        }


        return "account";
    }

    @RequestMapping(value = "/cancelDelivery", method = RequestMethod.GET)
    public String cancelDelivery(@RequestParam(name="deliveryId")long deliveryId,Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Account account = accountService.getAccountByEmail(auth.getName());
        Delivery deliveryToDelete = deliveryService.getDeliveryById(deliveryId);
        account.removeDelivery(deliveryToDelete);
        deliveryService.deleteDelivery(deliveryId);
        model.addAttribute("unpaiedDeliverys", account.getDeliveryList().stream().filter(delivery -> delivery.getPaied() == false).collect(Collectors.toList()));
        return "account";
    }
    @RequestMapping(value = "/payDelivery", method = RequestMethod.GET)
    public String payDelivery(@RequestParam(name="deliveryId")long deliveryId) {
        Delivery deliveryToPay = deliveryService.getDeliveryById(deliveryId);
        return "account";
    }
    @RequestMapping(value="/updateAccount",method =  RequestMethod.POST)
    public String updateAccount(Account account, Model model){
        try{
            model.addAttribute("account",accountService.updateAccount(account));
        }
        catch(Exception e)
        {
            model.addAttribute("response","An Error ocurred while updating your Account Details");
            model.addAttribute(account);
        }
    }
}
