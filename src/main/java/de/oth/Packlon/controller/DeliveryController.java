package de.oth.Packlon.controller;

import de.oth.Packlon.entity.Account;
import de.oth.Packlon.entity.Delivery;
import de.oth.Packlon.entity.LineItem;
import de.oth.Packlon.entity.Pack;
import de.oth.Packlon.service.AccountService;
import de.oth.Packlon.service.CustomerService;
import de.oth.Packlon.service.DeliveryService;
import de.oth.Packlon.service.PackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.sound.sampled.Line;
import java.util.ArrayList;
import java.util.List;

@Controller
public class DeliveryController {
    @Autowired
    private DeliveryService deliveryService;
    @Autowired
    private PackService packService;
    @Autowired
    private AccountService accountService;

    @RequestMapping(value = "/delivery", method = RequestMethod.GET)
    public String delivery(Model model) {
        Delivery delivery = new Delivery();

        model.addAttribute("delivery", delivery);
        return "delivery";
    }

    @RequestMapping(value = "/createDelivery", method = RequestMethod.POST)
    public String createDelivery(Delivery delivery,
                            @ModelAttribute("packageS") int amountPackageS,
                            @ModelAttribute("packageM") int amountPackageM,
                            @ModelAttribute("packageL") int amountPackageL,
                            Model model) {
        try{
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            Account account = accountService.getAccountByEmail(auth.getName());
            delivery.setSender(account.getOwner());
            delivery.setSenderAddress(account.getHomeAddress());
            if (amountPackageS != 0) {
                LineItem packageS = new LineItem();
                packageS.setAmount(amountPackageS);
                Pack packS = packService.getPackBySize("S");
                packageS.setPack(packS);
                packageS.calculatePrice();
                delivery.addLineItem(packageS);
            }
            if (amountPackageM != 0) {
                LineItem packageM = new LineItem();
                packageM.setAmount(amountPackageM);
                packageM.setPack(packService.getPackBySize("M"));
                packageM.calculatePrice();
                delivery.addLineItem(packageM);
            }
            if (amountPackageL != 0) {
                LineItem packageL = new LineItem();

                packageL.setPack(packService.getPackBySize("L"));
                packageL.setAmount(amountPackageL);
                packageL.calculatePrice();
                delivery.addLineItem(packageL);
            }
            deliveryService.createDelivery(delivery);
            accountService.addDelivery(account.getId(),delivery);
            model.addAttribute("message",
                    "You succesfully created a Delivery and can now find your Delivery in your Account and pay it");
            return "redirect:/delivery";
        } catch (Exception e){
            model.addAttribute("message",
                    "An Error Occured while creating your Delivery Error: " + e.getMessage());
            return "redirect:/delivery";
        }

    }
}
