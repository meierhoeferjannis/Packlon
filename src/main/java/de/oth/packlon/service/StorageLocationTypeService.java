package de.oth.packlon.service;


import de.oth.packlon.entity.StorageLocationType;
import de.oth.packlon.repository.StorageLocationTypeRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StorageLocationTypeService {
    private final StorageLocationTypeRepository storageLocationTypeRepository;

    public StorageLocationTypeService(StorageLocationTypeRepository storageLocationTypeRepository) {
        this.storageLocationTypeRepository = storageLocationTypeRepository;
    }

    public List<StorageLocationType> getAllStorageLocationTypes() {
        List<StorageLocationType> result = new ArrayList<StorageLocationType>();
        storageLocationTypeRepository.findAll().forEach(result::add);
        return result;

    }
}
