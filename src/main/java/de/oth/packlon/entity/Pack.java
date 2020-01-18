package de.oth.packlon.entity;

import javax.persistence.Entity;
import java.io.Serializable;

@Entity
public class Pack extends SingelIdEntity<Long> implements Serializable {

    private int price;
    private String size;

    public Pack() {
    }

    public Pack(int price, String size) {
        this.price = price;
        this.size = size;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }


}
