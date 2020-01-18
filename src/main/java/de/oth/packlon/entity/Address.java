package de.oth.packlon.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Embeddable
public class Address extends SingelIdEntity<Long> implements Serializable {

    private String street;
    private String country;
    private int postCode;
    private String city;
    private String addition;

    public Address(String street, String country, int postCode, String city) {
        this.street = street;
        this.country = country;
        this.postCode = postCode;
        this.city = city;
    }
    public Address(){}

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getPostCode() {
        return postCode;
    }

    public void setPostCode(int postCode) {
        this.postCode = postCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddition() {
        return addition;
    }

    public void setAddition(String addition) {
        this.addition = addition;
    }

}
