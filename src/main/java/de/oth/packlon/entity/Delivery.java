package de.oth.packlon.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Entity
public class Delivery extends SingelIdEntity<Long> implements Serializable {

    @Temporal(TemporalType.DATE)
    private Date submitted;
    @Temporal(TemporalType.DATE)
    private Date delivered;

    private int cashOndeliveryAmount;

    @ManyToOne
    private Customer receiver;
    @ManyToOne
    private Customer sender;
    @ManyToOne
    private Address senderAddress;
    @ManyToOne
    private Address receiverAddress;
    @ManyToOne
    private StorageLocation storageLocation;
    private boolean cashOnDelivery;
    @Temporal(TemporalType.DATE)
    private Date paymentDate;
    @NotNull
    private boolean paid;
    @OneToMany(fetch = FetchType.EAGER,cascade = {CascadeType.ALL})
    private List<Status> statusList;
    private String paymentReference;
    @OneToMany(cascade = {CascadeType.ALL})
    private List<LineItem> lineItemList;

    public Delivery() {
        lineItemList = new ArrayList<LineItem>();
        statusList = new ArrayList<Status>();
        paid = false;

    }

    public int getCashOndeliveryAmount() {
        return cashOndeliveryAmount;
    }

    public void setCashOndeliveryAmount(int cashOndeliveryAmount) {
        this.cashOndeliveryAmount = cashOndeliveryAmount;
    }

    public int totalPrice() {
        int total = 0;
        if (lineItemList.isEmpty())
            return 0;
        for (LineItem item : lineItemList) {
            item.calculatePrice();
            total += item.getPrice();
        }
        return total;
    }

    public float totalPriceFormatted() {
        if (lineItemList.isEmpty())
            return 0;
        return totalPrice() / 100;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public Address getSenderAddress() {
        return senderAddress;
    }

    public void setSenderAddress(Address senderAddress) {
        this.senderAddress = senderAddress;
    }

    public Address getReceiverAddress() {
        return receiverAddress;
    }

    public void setReceiverAddress(Address receiverAddress) {
        this.receiverAddress = receiverAddress;
    }

    public List<LineItem> getLineItemList() {
        return Collections.unmodifiableList(lineItemList);
    }

    public void setLineItemList(List<LineItem> lineItemList) {
        this.lineItemList = lineItemList;
    }


    public List<Status> getStatusList() {
        return Collections.unmodifiableList((statusList));
    }

    public void setStatusList(List<Status> statusList) {
        this.statusList = statusList;
    }


    public Date getSubmitted() {
        return submitted;
    }

    public void setSubmitted(Date submitted) {
        this.submitted = submitted;
    }

    public Date getDelivered() {
        return delivered;
    }

    public void setDelivered(Date delivered) {
        this.delivered = delivered;
    }

    public Customer getReceiver() {
        return receiver;
    }

    public void setReceiver(Customer receiver) {
        this.receiver = receiver;
    }

    public Customer getSender() {
        return sender;
    }

    public void setSender(Customer sender) {
        this.sender = sender;
    }

    public StorageLocation getStorageLocation() {
        return storageLocation;
    }

    public void setStorageLocation(StorageLocation storageLocation) {
        this.storageLocation = storageLocation;
    }

    public String getPaymentReference() {
        return paymentReference;
    }

    public void setPaymentReference(String paymentReference) {
        this.paymentReference = paymentReference;
    }

    public boolean isCashOnDelivery() {
        return cashOnDelivery;
    }

    public void setCashOnDelivery(boolean cashOnDelivery) {
        this.cashOnDelivery = cashOnDelivery;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public void addStatus(Status status) {
        if (!statusList.contains(status)) {
            statusList.add(status);
        }
    }

    public void removeStatus(Status status) {
        if (statusList.contains(status)) {
            statusList.remove(status);
        }
    }

    public void addLineItem(LineItem lineItem) {
        if (!lineItemList.contains(lineItem)) {
            lineItemList.add(lineItem);
        }
    }

    public void removeLineItem(LineItem lineItem) {
        if (lineItemList.contains(lineItem))
            lineItemList.remove(lineItem);
    }

}
