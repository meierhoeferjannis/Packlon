package de.oth.packlon.controller;

import de.oth.packlon.entity.Delivery;
import de.oth.packlon.service.DeliveryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class PayDeliveryController {
    private final DeliveryService deliveryService;

    public PayDeliveryController(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    @RequestMapping(value = "/payDelivery", method = RequestMethod.GET)
    public String payDelivery(Model model, @RequestParam(name = "deliveryId") long deliveryId) {
            model.addAttribute("delivery", deliveryService.getDeliveryById(deliveryId));
        return "payDelivery";
    }

    @RequestMapping(value = "/pay", method = RequestMethod.POST)
    public String pay(Model model,
                      @RequestParam(name = "deliveryId") long deliveryId,
                      @ModelAttribute(name = "password") String password,
                      @ModelAttribute(name = "username") String username,
                      RedirectAttributes redirectAttributes) {
        Delivery delivery = deliveryService.getDeliveryById(deliveryId);
        try {

            deliveryService.payDelivery(delivery,username,password);

            redirectAttributes.addFlashAttribute("success", "You succesfully paid your delivery. You can now download you label under Account->Paid Deliverys");
            return "redirect:/index";
        } catch (Exception e) {
            model.addAttribute("error", "An error occurred: " + e.getMessage());
            model.addAttribute("delivery", delivery);
            return "payDelivery";
        }


    }
}
