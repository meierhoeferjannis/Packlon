package de.oth.Packlon.entity;

import javax.persistence.*;

@Entity
public class StorageLocation extends SingelIdEntity<Long> {

    private String name;
    @OneToOne
    private Address address;
    @ManyToOne
    private StorageLocationType storageLocationType;

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public StorageLocationType getStorageLocationType() {
        return storageLocationType;
    }

    public void setStorageLocationType(StorageLocationType storageLocationType) {
        this.storageLocationType = storageLocationType;
    }



}
