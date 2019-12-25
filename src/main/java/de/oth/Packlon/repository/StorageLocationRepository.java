package de.oth.Packlon.repository;

import de.oth.Packlon.entity.StorageLocation;
import org.springframework.data.repository.CrudRepository;

import javax.annotation.Resource;

@Resource
public interface StorageLocationRepository extends CrudRepository<StorageLocation, Long> {
   public Iterable<StorageLocation>findAllByAddress_PostCode(int postCode);

}
