package de.oth.Packlon.entity;

import javax.persistence.*;

@Entity
public class LineItem extends SingelIdEntity<Long> {

    private int amount;
    private int price;
    @ManyToOne
    private Pack pack;

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
