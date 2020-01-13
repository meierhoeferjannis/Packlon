package de.oth.packlon.repository;

import de.oth.packlon.entity.StorageLocation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import javax.annotation.Resource;

@Resource
public interface StorageLocationRepository extends PagingAndSortingRepository<StorageLocation, Long> {
   public Iterable<StorageLocation>findAllByAddress_PostCode(int postCode);
   Page<StorageLocation> findAll(Pageable pageable);

}
