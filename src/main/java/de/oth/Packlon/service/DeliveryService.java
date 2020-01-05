package de.oth.Packlon.service;


import de.oth.Packlon.entity.Address;
import de.oth.Packlon.entity.Delivery;
import de.oth.Packlon.entity.LineItem;
import de.oth.Packlon.entity.Status;
import de.oth.Packlon.repository.DeliveryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Service
public class DeliveryService {
    @Autowired
    private DeliveryRepository deliveryRepository;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private LineItemService lineItemService;
    @Autowired
    private AddressService addressService;


    public Delivery createDelivery(Delivery delivery) {
        delivery.setReceiver(customerService.getCustomerByName(delivery.getReceiver()));
        delivery.setSender(customerService.getCustomerByName(delivery.getSender()));
        List<LineItem> items = new ArrayList<LineItem>();
        for (LineItem item : delivery.getLineItemList()) {
            items.add(lineItemService.createLineItem(item));
        }
        delivery.setLineItemList(items);
        Address recAddress;
        Address sendAddress;
        if (addressService.existsAddress(delivery.getReceiverAddress())) {
            recAddress = addressService.getAddress(delivery.getReceiverAddress());
        } else {
            delivery.setReceiverAddress(addressService.createAddress(delivery.getReceiverAddress()));
        }
        if (addressService.existsAddress(delivery.getSenderAddress())) {
            sendAddress = addressService.getAddress(delivery.getSenderAddress());
        } else {
            sendAddress = addressService.createAddress(delivery.getSenderAddress());
        }
        Status status = new Status();
        status.setText("Delivery Created");

        delivery.addStatus(status);
        return deliveryRepository.save(delivery);

    }
    public void deleteDelivery(long deliveryId){
         deliveryRepository.deleteById(deliveryId);
    }
    public Delivery getDeliveryById(long deliveryId){
        return deliveryRepository.findById(deliveryId).get();
    }

    public Delivery updateDelivery(Delivery delivery) {
        return deliveryRepository.save(delivery);
    }
//public Delivery requestDelivery(Delivery delivery){}
//public boolean payDelivery(Delivery delivery){


}
