package de.oth.packlon.controller;

import de.oth.packlon.entity.Delivery;
import de.oth.packlon.entity.Status;
import de.oth.packlon.entity.StorageLocation;
import de.oth.packlon.service.DeliveryService;
import de.oth.packlon.service.StorageLocationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class TrackDeliveryController {
    private final DeliveryService deliveryService;
    private final StorageLocationService storageLocationService;

    public TrackDeliveryController(DeliveryService deliveryService, StorageLocationService storageLocationService) {
        this.deliveryService = deliveryService;
        this.storageLocationService = storageLocationService;
    }

    public void addLocationPage(Model model, int pageLocation) {
        model.addAttribute("pageLcoation",pageLocation);
        if (pageLocation != 0) {
            Page<StorageLocation> storageLocations = storageLocationService.getStorageLocationPage(PageRequest.of(pageLocation - 1, 3));
            model.addAttribute("storageLocations", storageLocations);
            int totalPages = storageLocations.getTotalPages();
            if (totalPages > 0) {
                List<Integer> pageNumbersUnpaid = IntStream.rangeClosed(1, totalPages)
                        .boxed()
                        .collect(Collectors.toList());
                model.addAttribute("pageNumbersLocation", pageNumbersUnpaid);
            }
        } else {
            model.addAttribute("storageLocations",  Page.empty());
        }

    }

    @RequestMapping(value = {"/trackDelivery"}, method = RequestMethod.GET)
    public String getTrackDeliveryView(Model model,
                                       @RequestParam(name = "deliveryId") Optional<Long> deliveryId,
                                       @RequestParam(name = "pageLocation") Optional<Integer> pageLocation) {
        if (deliveryId.isPresent()) {
            model.addAttribute("deliveryStatus", deliveryService.getDeliveryById(deliveryId.get()).getStatusList());
        } else {
            model.addAttribute("deliveryStatus", new ArrayList<Status>());
        }
        addLocationPage(model, pageLocation.orElse(0));
        return "trackDelivery";
    }


    @RequestMapping(value = {"/track"}, method = RequestMethod.POST)
    public String track(@ModelAttribute("deliveryId") long deliveryId, Model model,@RequestParam(name = "pageLocation") Optional<Integer> pageLocation) {

        addLocationPage(model,pageLocation.orElse(1));
        try {
            model.addAttribute("deliveryStatus", deliveryService.getDeliveryById(deliveryId).getStatusList());

            model.addAttribute("deliveryId", deliveryId);
        } catch (Exception e) {
            model.addAttribute("deliveryStatus", new ArrayList<Status>());

            model.addAttribute("response", "Your Delivery was not found");
        }
        return "trackDelivery";
    }

    @RequestMapping(value = {"/changeStorageLocation"}, method = RequestMethod.GET)
    public String changeStorageLocation(@RequestParam(name = "storageLocationId") long storageLocationId,
                                        Model model,
                                        @RequestParam(name = "deliveryId")long deliveryId) {
        Delivery delivery = deliveryService.getDeliveryById(deliveryId);
        StorageLocation storageLocation = storageLocationService.getStorageLocationById(storageLocationId);
        delivery.setStorageLocation(storageLocation);
        Status status = new Status();
        status.setText("You change the Destination to Storage Location: " + storageLocation.getName() + " "
                + storageLocation.getStorageLocationType().getName() +
                " Address: "
                + storageLocation.getAddress().getCountry() + " "
                + storageLocation.getAddress().getCity() + " "
                + storageLocation.getAddress().getPostCode() + " "
                + storageLocation.getAddress().getStreet() + " "
                + storageLocation.getAddress().getAddition());
        delivery.addStatus(status);
        deliveryService.updateDelivery(delivery);
        model.addAttribute("response", "Your delivery with " + deliveryId + " was redirected to the Storage Location" + storageLocation.getName());
        model.addAttribute("deliveryStatus", deliveryService.getDeliveryById(deliveryId).getStatusList());
       addLocationPage(model,0);

        return "trackDelivery";
    }

}
