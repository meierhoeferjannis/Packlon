package de.oth.packlon.service;


import de.oth.packlon.entity.StorageLocation;
import de.oth.packlon.repository.StorageLocationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StorageLocationService implements IStorageLocationService {
    private final StorageLocationRepository storageLocationRepository;

    public StorageLocationService(StorageLocationRepository storageLocationRepository) {
        this.storageLocationRepository = storageLocationRepository;
    }

    @Override
    public List<StorageLocation> getStorageLocationsByPostCode(int postCode){
        List<StorageLocation> result = new ArrayList<StorageLocation>();

         storageLocationRepository.findAllByAddress_PostCode(postCode).forEach(result::add);
         return result;
    }
    @Override
    public Page<StorageLocation> getStorageLocationPage(Pageable pageable){
        return storageLocationRepository.findAll(pageable);
    }
    @Override
    public List<StorageLocation> getAllStorageLocations() {
        List<StorageLocation> result = new ArrayList<StorageLocation>();

         storageLocationRepository.findAll().forEach(result::add);
         return  result;
    }
    @Override
    public StorageLocation getStorageLocationById(long storageLocationId){
        return storageLocationRepository.findById(storageLocationId).get();
    }

}
