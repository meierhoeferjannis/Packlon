package de.oth.Packlon.repository;

import de.oth.Packlon.entity.StorageLocation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import javax.annotation.Resource;

@Resource
public interface StorageLocationRepository extends PagingAndSortingRepository<StorageLocation, Long> {
   public Iterable<StorageLocation>findAllByAddress_PostCode(int postCode);
   Page<StorageLocation> findAll(Pageable pageable);

}
