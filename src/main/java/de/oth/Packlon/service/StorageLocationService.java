package de.oth.Packlon.service;


import de.oth.Packlon.entity.StorageLocation;
import de.oth.Packlon.repository.StorageLocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StorageLocationService {
    @Autowired
    private StorageLocationRepository storageLocationRepository;
    public List<StorageLocation> getStorageLocationsByPostCode(int postCode){
        List<StorageLocation> result = new ArrayList<StorageLocation>();

         storageLocationRepository.findAllByAddress_PostCode(postCode).forEach(result::add);
         return result;
    }
    public List<StorageLocation> getAllStorageLocations() {
        List<StorageLocation> result = new ArrayList<StorageLocation>();

         storageLocationRepository.findAll().forEach(result::add);
         return  result;
    }
    public StorageLocation getStorageLocationById(long storageLocationId){
        return storageLocationRepository.findById(storageLocationId).get();
    }

}
