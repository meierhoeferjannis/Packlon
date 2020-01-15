package de.oth.packlon.service;

import de.oth.packlon.entity.Delivery;
import de.oth.packlon.entity.Status;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class Postman {
    private final DeliveryService deliveryService;

    private final List<Delivery> submittedDeliveries = new ArrayList<Delivery>();
    private final List<Delivery> inCarLoadedDeliveries = new ArrayList<Delivery>();
    private final List<Delivery> onTheWayDeliveries = new ArrayList<Delivery>();

    public Postman(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    @Scheduled(fixedRate = 120000)
    public void submit() {
        try{
            List<Delivery> deliveries = deliveryService.getDeliveriesToDeliver();
            if (!deliveries.isEmpty()) {
                for (Delivery item : deliveries) {
                    item.setSubmitted(new Date());
                    item.addStatus(new Status("Your Delivery was submitted"));
                    deliveryService.updateDelivery(item);
                    submittedDeliveries.add(item);
                }
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }


    }

    @Scheduled(fixedRate = 120000, initialDelay = 60000)
    public void loadInCar() {
        if (!submittedDeliveries.isEmpty())
            for (Delivery item : submittedDeliveries) {
                item.addStatus(new Status("Your Delivery was loaded in the Car"));
                deliveryService.updateDelivery(item);
                inCarLoadedDeliveries.add(item);
            }
        submittedDeliveries.clear();
    }
    @Scheduled(fixedRate = 120000,initialDelay = 120000)
    public void transport(){
        if (!inCarLoadedDeliveries.isEmpty()){
            for (Delivery item: inCarLoadedDeliveries){
                item.addStatus(new Status("Your Delivery is now on the way"));
                deliveryService.updateDelivery(item);
                onTheWayDeliveries.add(item);
            }
            inCarLoadedDeliveries.clear();
        }
    }
    @Scheduled(fixedRate = 120000,initialDelay = 180000)
    public void deliver(){
        if (!onTheWayDeliveries.isEmpty()){
            for (Delivery item: onTheWayDeliveries){
                item.addStatus(new Status("Your Delivery was succesfully deliverd"));
                deliveryService.updateDelivery(item);
            }
            onTheWayDeliveries.clear();
        }
    }
}
