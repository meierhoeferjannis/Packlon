package de.oth.Packlon.entity;

import javax.persistence.Entity;

@Entity
public class StorageLocationType extends SingelIdEntity<Long> {

    private String name;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
