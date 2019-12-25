package de.oth.Packlon.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Status extends SingelIdEntity<Long> {

    private String text;
    @Temporal(TemporalType.DATE)
    private Date statusCreated;


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getStatusCreated() {
        return statusCreated;
    }

    public void setStatusCreated(Date statusCreated) {
        this.statusCreated = statusCreated;
    }


}
