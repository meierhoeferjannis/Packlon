package de.oth.Packlon.service;


import de.oth.Packlon.entity.*;
import de.oth.Packlon.repository.DeliveryRepository;
import de.oth.Packlon.service.model.DeliveryRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class DeliveryService {
    @Autowired
    private AccountService accountService;
    @Autowired
    private DeliveryRepository deliveryRepository;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private LineItemService lineItemService;
    @Autowired
    private AddressService addressService;
    @Autowired
    private PackService packService;

    public Page<Delivery> getDeliveryPageForSender(boolean paid, Customer sender, Pageable pageable) {
        return deliveryRepository.findAllByPaidAndSender(paid, sender, pageable);
    }

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

    public void deleteDelivery(long deliveryId) {
        deliveryRepository.deleteById(deliveryId);
    }

    public Delivery getDeliveryById(long deliveryId) {
        return deliveryRepository.findById(deliveryId).get();
    }

    public Delivery updateDelivery(Delivery delivery) {
        return deliveryRepository.save(delivery);
    }

    public long requestDelivery(Delivery delivery, Account account) throws DeliveryRequestException {
        try {
            Delivery newDelivery = new Delivery();
            if (newDelivery.getSenderAddress() == null) {
                newDelivery.setSenderAddress(account.getHomeAddress());
            } else {
                if (addressService.existsAddress(newDelivery.getSenderAddress())) {
                    newDelivery.setSenderAddress(addressService.getAddress(newDelivery.getSenderAddress()));
                } else {
                    newDelivery.setSenderAddress(addressService.createAddress(newDelivery.getSenderAddress()));
                }
            }
            if (addressService.existsAddress(newDelivery.getReceiverAddress())) {
                newDelivery.setReceiverAddress(addressService.getAddress(newDelivery.getReceiverAddress()));
            } else {
                newDelivery.setReceiverAddress(addressService.createAddress(newDelivery.getReceiverAddress()));
            }
            if (newDelivery.getSender() == null) {
                newDelivery.setSender(account.getOwner());
            } else {
                newDelivery.setSender(customerService.getCustomerByName(newDelivery.getSender()));
            }
            newDelivery.setReceiver(customerService.getCustomerByName(newDelivery.getReceiver()));
            for (LineItem item : delivery.getLineItemList()) {
                if (item.getAmount() != 0) {
                    LineItem lineItem = new LineItem(item.getAmount(), packService.getPackBySize(item.getPack().getSize()));
                    lineItem.calculatePrice();
                    newDelivery.addLineItem(lineItem);
                }
            }
            Status status = new Status();
            status.setText("Delivery Created per Request");
            newDelivery.addStatus(status);
            return deliveryRepository.save(newDelivery).id;
        } catch (Exception e) {
            throw new DeliveryRequestException(e.getMessage());
        }

    }

    public Delivery payDelivery(Delivery delivery) {
        int amount = 0;
        for (LineItem l : delivery.getLineItemList()) {
            amount += l.getPrice();
        }
        delivery.setPaid(true);
        return deliveryRepository.save(delivery);
    }


}
