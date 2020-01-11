package de.oth.Packlon.controller;

import de.oth.Packlon.entity.Account;
import de.oth.Packlon.entity.Delivery;
import de.oth.Packlon.service.AccountService;
import de.oth.Packlon.service.DeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class DeliveryRestController {
    @Autowired
    private DeliveryService deliveryService;
    @Autowired
    private AccountService accountService;
    @RequestMapping(value="/restapi/delivery",method = RequestMethod.POST)
    public long post(Delivery delivery, Principal principal){
        try{

        }catch (Exception e){

        }
        Account account = accountService.getAccountByEmail(principal.getName());
        return deliveryService.requestDelivery(delivery,account);
    }
}
