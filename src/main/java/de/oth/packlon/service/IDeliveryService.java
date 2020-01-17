package de.oth.packlon.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.itextpdf.text.DocumentException;
import de.oth.packlon.entity.Account;
import de.oth.packlon.entity.Customer;
import de.oth.packlon.entity.Delivery;
import de.oth.packlon.service.model.DeliveryRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface IDeliveryService {
    Page<Delivery> getDeliveryPageForSender(boolean paid, Customer sender, Pageable pageable);

    Delivery createDelivery(Delivery delivery);

    void deleteDelivery(long deliveryId);

    Delivery getDeliveryById(long deliveryId);

    Delivery updateDelivery(Delivery delivery);

    long requestDelivery(Delivery delivery ) throws DeliveryRequestException;

    Delivery payDelivery(Delivery delivery, String username, String password) throws JsonProcessingException;

    InputStream createPDFforDelivery(long deliveryId) throws IOException, DocumentException;

    List<Delivery> getDeliveriesToDeliver();
}
