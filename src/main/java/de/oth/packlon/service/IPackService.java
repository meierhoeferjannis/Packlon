package de.oth.packlon.service;

import de.oth.packlon.entity.Pack;

import java.util.List;

public interface IPackService {
    List<Pack> getAllPacks();

    Pack getPackById(long packId);

    Pack getPackBySize(String size);
}
