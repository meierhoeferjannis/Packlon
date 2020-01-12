package de.oth.Packlon.controller;

import de.oth.Packlon.entity.Account;
import de.oth.Packlon.entity.Delivery;
import de.oth.Packlon.service.AccountService;
import de.oth.Packlon.service.DeliveryService;
import de.oth.Packlon.service.model.DeliveryRequestException;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
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

    @RequestMapping(value = "/restapi/delivery", method = RequestMethod.POST)
    public ResponseEntity<Long> postDelivery(@RequestBody Delivery delivery, Principal principal) throws DeliveryRequestException {
        long ret = deliveryService.requestDelivery(delivery, accountService.getAccountByEmail(principal.getName()));
        return new ResponseEntity<>(ret, HttpStatus.OK);
    }
}
