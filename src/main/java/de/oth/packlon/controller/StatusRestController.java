package de.oth.packlon.controller;

import de.oth.packlon.entity.Delivery;
import de.oth.packlon.entity.Status;
import de.oth.packlon.service.DeliveryService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class StatusRestController {
    private final DeliveryService deliveryService;

    public StatusRestController(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }
    @RequestMapping(value = "/restapi/status/{deliveryid}", method = RequestMethod.GET)
    public ResponseEntity<List<Status>> getStatus(@PathVariable("deliveryid") long deliveryid)   {
        try {
            Delivery delivery = deliveryService.getDeliveryById(deliveryid);
            return new ResponseEntity<>(delivery.getStatusList(), HttpStatus.OK);
        }catch(Exception e){
         return new ResponseEntity<>(new ArrayList<>(),HttpStatus.NOT_FOUND);
        }

    }

}
