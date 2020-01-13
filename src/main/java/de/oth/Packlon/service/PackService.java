package de.oth.Packlon.service;


import de.oth.Packlon.entity.Pack;
import de.oth.Packlon.repository.PackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PackService {
    private final PackRepository packRepository;

    public PackService(PackRepository packRepository) {
        this.packRepository = packRepository;
    }

    public List<Pack> getAllPacks(){
        List<Pack> result = new ArrayList<Pack>();
          packRepository.findAll().forEach(result::add);
          return result;
    }
    public Pack getPackById(long packId){
        Optional<Pack> pack = packRepository.findById(packId);

            return pack.get();

    }
    public Pack getPackBySize(String size){
        return packRepository.findPackBySizeEquals(size);

    }
}
