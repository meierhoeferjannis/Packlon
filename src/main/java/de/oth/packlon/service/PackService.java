package de.oth.packlon.service;


import de.oth.packlon.entity.Pack;
import de.oth.packlon.repository.PackRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PackService implements IPackService {
    private final PackRepository packRepository;

    public PackService(PackRepository packRepository) {
        this.packRepository = packRepository;
    }

    @Override
    public List<Pack> getAllPacks(){
        List<Pack> result = new ArrayList<Pack>();
          packRepository.findAll().forEach(result::add);
          return result;
    }
    @Override
    public Pack getPackById(long packId){
        Optional<Pack> pack = packRepository.findById(packId);

            return pack.get();

    }
    @Override
    public Pack getPackBySize(String size){
        return packRepository.findPackBySizeEquals(size);

    }
}
