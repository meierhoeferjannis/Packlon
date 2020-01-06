package de.oth.Packlon.controller;

import de.oth.Packlon.entity.Account;
import de.oth.Packlon.entity.Delivery;
import de.oth.Packlon.service.AccountService;
import de.oth.Packlon.service.AddressService;
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
    @Autowired
    private AddressService addressService;
    @RequestMapping(value = {"/account"}, method = RequestMethod.GET)
    public String account(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Account account = accountService.getAccountByEmail(auth.getName());
        try {
            model.addAttribute("account", account);
            model.addAttribute("paiedDeliverys", account.getDeliveryList().stream().filter(delivery -> delivery.getPaied() == true).collect(Collectors.toList()));
            model.addAttribute("unpaiedDeliverys", account.getDeliveryList().stream().filter(delivery -> delivery.getPaied() == false).collect(Collectors.toList()));
        } catch (Exception e) {
            model.addAttribute("account", account);
            model.addAttribute("unpaiedDeliverys", new ArrayList<Delivery>());
            model.addAttribute("paiedDeliverys", new ArrayList<Delivery>());
        }


        return "account";
    }

    @RequestMapping(value = "/cancelDelivery", method = RequestMethod.GET)
    public String cancelDelivery(@RequestParam(name = "deliveryId") long deliveryId, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Account account = accountService.getAccountByEmail(auth.getName());

        try{
            Delivery deliveryToDelete = deliveryService.getDeliveryById(deliveryId);
            account.removeDelivery(deliveryToDelete);
            deliveryService.deleteDelivery(deliveryId);
        }
        catch (Exception e){
            model.addAttribute("response", "An Error ocurred while deleting your Delivery");
        }

        model.addAttribute("paiedDeliverys", account.getDeliveryList().stream().filter(delivery -> delivery.getPaied() == true).collect(Collectors.toList()));
        model.addAttribute("unpaiedDeliverys", account.getDeliveryList().stream().filter(delivery -> delivery.getPaied() == false).collect(Collectors.toList()));        return "account";
    }

    @RequestMapping(value = "/payDelivery", method = RequestMethod.GET)
    public String payDelivery(@RequestParam(name = "deliveryId") long deliveryId) {
        Delivery deliveryToPay = deliveryService.getDeliveryById(deliveryId);
        return "account";
    }

    @RequestMapping(value = "/updateAccount", method = RequestMethod.POST)
    public String updateAccount(Account updatedAccount, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Account account = accountService.getAccountByEmail(auth.getName());
        model.addAttribute("paiedDeliverys", account.getDeliveryList().stream().filter(delivery -> delivery.getPaied() == true).collect(Collectors.toList()));
        model.addAttribute("unpaiedDeliverys", account.getDeliveryList().stream().filter(delivery -> delivery.getPaied() == false).collect(Collectors.toList()));
        try {

            if(addressService.existsAddress(updatedAccount.getHomeAddress())){
                account.setHomeAddress(addressService.getAddress(updatedAccount.getHomeAddress()));
            }
            else{
                account.setHomeAddress(addressService.createAddress(updatedAccount.getHomeAddress()));
            }
           account.setPhone(updatedAccount.getPhone());
            model.addAttribute("account", accountService.updateAccount(account));

            return "account";
        } catch (Exception e) {
            model.addAttribute("response", "An Error ocurred while updating your Account Details");
            model.addAttribute(account);
            return "account";
        }
    }
}
