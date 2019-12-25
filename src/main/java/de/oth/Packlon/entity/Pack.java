package de.oth.Packlon.entity;

import javax.persistence.Entity;

@Entity
public class Pack extends SingelIdEntity<Long> {

    private int price;
    private String size;

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
