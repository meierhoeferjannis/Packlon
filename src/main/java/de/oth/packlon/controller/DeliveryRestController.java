package de.oth.packlon.controller;

import de.oth.packlon.entity.Delivery;
import de.oth.packlon.service.AccountService;
import de.oth.packlon.service.DeliveryService;
import de.oth.packlon.service.model.DeliveryRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class DeliveryRestController {
    private final DeliveryService deliveryService;
    private final AccountService accountService;

    public DeliveryRestController(DeliveryService deliveryService, AccountService accountService) {
        this.deliveryService = deliveryService;
        this.accountService = accountService;
    }

    @RequestMapping(value = "/restapi/delivery", method = RequestMethod.POST)
    public ResponseEntity<Long> postDelivery(@RequestBody Delivery delivery, Principal principal) throws DeliveryRequestException {
        long ret = deliveryService.requestDelivery(delivery, accountService.getAccountByEmail(principal.getName()));
        return new ResponseEntity<>(ret, HttpStatus.OK);
    }

}
