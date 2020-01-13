package de.oth.packlon.repository;

import de.oth.packlon.entity.Pack;
import org.springframework.data.repository.CrudRepository;

public interface PackRepository extends CrudRepository<Pack, Long> {
    public Pack findPackBySizeEquals(String size);
}
