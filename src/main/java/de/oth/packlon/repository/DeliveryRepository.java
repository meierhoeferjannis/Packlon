package de.oth.packlon.repository;

import de.oth.packlon.entity.Customer;
import de.oth.packlon.entity.Delivery;
import org.hibernate.cache.spi.entry.StructuredCacheEntry;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.PagingAndSortingRepository;

import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DeliveryRepository extends PagingAndSortingRepository<Delivery, Long> {

    Page<Delivery> findAllByPaidAndSender(boolean paid, Customer sender, Pageable pageable);
    List<Delivery> findAllByPaidAndSubmittedIsNull(boolean paid);
    List<Delivery> findAllByCashOnDeliveryOrPaidAndSubmittedIsNull(boolean cashOnDelivery,boolean paid);

}
