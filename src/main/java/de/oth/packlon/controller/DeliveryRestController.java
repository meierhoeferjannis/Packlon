package de.oth.packlon.controller;

import com.itextpdf.text.DocumentException;
import de.oth.packlon.entity.Delivery;
import de.oth.packlon.service.AccountService;
import de.oth.packlon.service.DeliveryService;
import de.oth.packlon.service.model.DeliveryRequestException;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
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
    public long postDelivery(@RequestBody Delivery delivery) throws DeliveryRequestException {

        return deliveryService.requestDelivery(delivery);

    }

    @RequestMapping(value = "/restapi/deliverylabel/{deliveryid}", method = RequestMethod.GET)
    public void getDeliveryLabel(@PathVariable("deliveryid") long deliveryId, HttpServletResponse response)  {
        try {

            InputStream is = deliveryService.createPDFforDelivery(deliveryId);

            IOUtils.copy(is, response.getOutputStream());
            response.flushBuffer();
            is.close();

            Files.delete(Path.of(deliveryId + ".pdf"));
            Files.delete(Path.of(deliveryId + "decrypted.pdf"));
        } catch (IOException | DocumentException ex) {
            System.out.println(ex.getMessage());
        }
    }
}


