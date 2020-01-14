package de.oth.packlon.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Status extends SingelIdEntity<Long> {

    private String text;
    @Temporal(TemporalType.DATE)
    private Date statusCreated;
    public Status(String text){
        statusCreated = new Date();
        this.text = text;
    }
    public Status(){statusCreated = new Date();}


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
