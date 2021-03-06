package de.oth.packlon.controller;

import de.oth.packlon.entity.Account;
import de.oth.packlon.entity.Delivery;
import de.oth.packlon.entity.LineItem;

import de.oth.packlon.service.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;


@Controller
public class DeliveryController {
    private final DeliveryService deliveryService;
    private final PackService packService;
    private final AccountService accountService;
    private final AddressService addressService;

    public DeliveryController(DeliveryService deliveryService, PackService packService, AccountService accountService, AddressService addressService) {
        this.deliveryService = deliveryService;
        this.packService = packService;
        this.accountService = accountService;
        this.addressService = addressService;
    }

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
                                 RedirectAttributes redirectAttributes) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            Account account = accountService.getAccountByEmail(auth.getName());
            if (account.isCompanyAccount()){
                delivery.setPaid(true);
                delivery.setPaymentDate(new Date());
            }
            delivery.setSender(account.getOwner());
            delivery.setSenderAddress(account.getHomeAddress());
            if (addressService.existsAddress(delivery.getReceiverAddress())) {
                delivery.setReceiverAddress(addressService.getAddress(delivery.getReceiverAddress()));
            } else {
                delivery.setReceiverAddress(addressService.createAddress(delivery.getReceiverAddress()));
            }
            if (amountPackageS != 0) {
                LineItem packageS = new LineItem(amountPackageS, packService.getPackBySize("S"));
                packageS.calculatePrice();
                delivery.addLineItem(packageS);
            }
            if (amountPackageM != 0) {
                LineItem packageM = new LineItem(amountPackageM, packService.getPackBySize("M"));
                packageM.calculatePrice();
                delivery.addLineItem(packageM);
            }
            if (amountPackageL != 0) {
                LineItem packageL = new LineItem(amountPackageL, packService.getPackBySize("L"));
                packageL.calculatePrice();
                delivery.addLineItem(packageL);
            }

            deliveryService.createDelivery(delivery);
            accountService.addDelivery(account.getId(), delivery);
            redirectAttributes.addFlashAttribute("success",
                    "You succesfully created a delivery and can now find it in your account and pay it");
            return "redirect:/delivery";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error",
                    "An Error occurred while creating your delivery error: " + e.getMessage());
            return "redirect:/delivery";
        }

    }
}
