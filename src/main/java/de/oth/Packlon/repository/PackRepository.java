package de.oth.Packlon.repository;

import de.oth.Packlon.entity.Pack;
import org.springframework.data.repository.CrudRepository;

public interface PackRepository extends CrudRepository<Pack, Long> {
    public Pack getBySizeIsLike(String size);
}
