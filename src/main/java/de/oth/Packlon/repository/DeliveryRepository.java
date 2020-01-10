package de.oth.Packlon.repository;

import de.oth.Packlon.entity.Customer;
import de.oth.Packlon.entity.Delivery;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.PagingAndSortingRepository;

import org.springframework.data.domain.Pageable;
import java.util.List;

public interface DeliveryRepository extends PagingAndSortingRepository<Delivery, Long> {

    Page<Delivery> findAllByPaidAndSender(boolean paid, Customer sender, Pageable pageable);

}
