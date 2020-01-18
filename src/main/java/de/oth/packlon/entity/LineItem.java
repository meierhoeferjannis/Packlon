package de.oth.packlon.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class LineItem extends SingelIdEntity<Long> implements Serializable {

    private int amount;
    private int price;
    @ManyToOne
    private Pack pack;

    public LineItem() {
    }

    public LineItem(int amount, Pack pack) {
        this.amount = amount;
        this.pack = pack;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;

    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Pack getPack() {
        return pack;
    }

    public void setPack(Pack pack) {
        this.pack = pack;
    }

    public void calculatePrice() {
        this.price = pack.getPrice() * amount;
    }

}
