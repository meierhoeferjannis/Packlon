package de.oth.Packlon.controller;

import de.oth.Packlon.entity.Delivery;
import de.oth.Packlon.service.DeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DeliveryRestController {
    @Autowired
    private DeliveryService deliveryService;
    @RequestMapping(value="/restapi/delivery",method = RequestMethod.POST)
    public long post(Delivery delivery){
        return deliveryService.createDelivery(delivery).id;
    }
}
