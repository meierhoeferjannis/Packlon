package de.oth.Packlon.controller;

import de.oth.Packlon.entity.Delivery;
import de.oth.Packlon.entity.Status;
import de.oth.Packlon.entity.StorageLocation;
import de.oth.Packlon.service.DeliveryService;
import de.oth.Packlon.service.StorageLocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;

@Controller
public class trackDeliveryController {
    @Autowired
    private DeliveryService deliveryService;
    @Autowired
    private StorageLocationService storageLocationService;

    @RequestMapping(value = {"/trackDelivery"}, method = RequestMethod.GET)
    public String getTrackDeliveryView(Model model) {
        model.addAttribute("deliveryStatus", new ArrayList<Status>());
        model.addAttribute("storageLocations", new ArrayList<StorageLocation>());
        return "trackDelivery";
    }


    @RequestMapping(value = {"/track"}, method = RequestMethod.POST)
    public String track(@ModelAttribute("deliveryId") long deliveryId, Model model) {
        try {
            model.addAttribute("deliveryStatus", deliveryService.getDeliveryById(deliveryId).getStatusList());
            model.addAttribute("storageLocations", storageLocationService.getAllStorageLocations());
            model.addAttribute("deliveryId", deliveryId);
        } catch (Exception e) {
            model.addAttribute("deliveryStatus", new ArrayList<Status>());
            model.addAttribute("storageLocations", new ArrayList<StorageLocation>());
            model.addAttribute("response", "Your Delivery was not found");
        }
        return "trackDelivery";
    }

    @RequestMapping(value = {"/changeStorageLocation"}, method = RequestMethod.GET)
    public String changeStorageLocation(@RequestParam(name = "storageLocationId") long storageLocationId,
                                        Model model, @RequestParam(name = "deliveryId")
                                                long deliveryId) {
        Delivery delivery = deliveryService.getDeliveryById(deliveryId);
        StorageLocation storageLocation = storageLocationService.getStorageLocationById(storageLocationId);
        delivery.setStorageLocation(storageLocation);
        Status status = new Status();
        status.setText("You change the Destination to Storage Location " + storageLocation.getName() + " "
                + storageLocation.getStorageLocationType().getName() +
                " Address"
                + storageLocation.getAddress().getCountry() + " "
                + storageLocation.getAddress().getCity() + " "
                + storageLocation.getAddress().getPostCode() + " "
                + storageLocation.getAddress().getStreet() + " "
                + storageLocation.getAddress().getAddition());
        delivery.addStatus(status);
        deliveryService.updateDelivery(delivery);
        model.addAttribute("response", "Your delivery with " + deliveryId + " was redirected to the Storage Location" + storageLocation.getName());
        model.addAttribute("deliveryStatus", deliveryService.getDeliveryById(deliveryId).getStatusList());
        model.addAttribute("storageLocations", storageLocationService.getAllStorageLocations());

        return "trackDelivery";
    }

}
