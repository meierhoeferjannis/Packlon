package de.oth.packlon.service;

import de.oth.packlon.entity.StorageLocation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IStorageLocationService {
    List<StorageLocation> getStorageLocationsByPostCode(int postCode);

    Page<StorageLocation> getStorageLocationPage(Pageable pageable);

    List<StorageLocation> getAllStorageLocations();

    StorageLocation getStorageLocationById(long storageLocationId);
}
