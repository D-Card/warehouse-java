package ggc.partners;

import java.io.*;
import java.util.*;
import java.text.Collator;
import java.util.Locale;
import ggc.exceptions.*;
import ggc.transactions.*;
import ggc.products.*;

public class Partner implements Serializable, Comparable<Partner>{

    private static final long serialVersionUID = 202110262342L;

    private String _name;
    private String _id;
    private String _address;
    private Status _status = new NormalStatus();
    private float _points = 0;
    private float _buyTotalValue = 0;
    private float _sellTotalValue = 0;
    private float _sellPaidValue = 0;
    private Mailbox _mailbox = new Mailbox();
    private PriorityQueue<Batch> _batches = new PriorityQueue<Batch>();

    // TBD - Datastructure for transaction history

    public Partner(String id, String name, String address) {
        _id = id;
        _name = name;
        _address = address;
    }

    // Getters
    public String getName() {
        return _name;
    }

    public String getId() {
        return _id;
    }

    public String getAddress() {
        return _address;
    }

    public Status getStatus() {
        return _status;
    }

    public float getPoints() {
        return _points;
    }

    public Mailbox getMailbox() {
        return _mailbox;
    }

    public List<Notification> listAllNotifications() { return _mailbox.listAllNotifications(); }

    public List<Notification> listAllNotificationsByMethod(String method) { return _mailbox.listNotificationsByMethod(method); }

    public PriorityQueue<Batch> getBatches() { return _batches; }

    // Setters
    public void setName(String name) {
        _name = name;
    }

    public void setId(String id) {
        _id = id;
    }

    public void setAddress(String address) {
        _address = address;
    }

    public void setStatus(Status status) {
        _status = status;
    }

    public void setPoints(float points) {
        _points = points;

        if (_points >= 25000) { setStatus(new EliteStatus()); }
        else if (_points >= 2000) { setStatus(new SelectionStatus()); }
        else setStatus(new NormalStatus());
    }

    public void setMailbox(Mailbox mailbox) {
        _mailbox = mailbox;
    }

    public void addBatch(Batch batch) { _batches.add(batch); }

    public void removeBatch(Batch batch) { _batches.remove(batch); }

    @Override
    public String toString() {
        return _id + "|" + _name + "|" + _address + "|" + _status + "|" + Math.round(_points) + "|" + Math.round(_buyTotalValue) + "|" + Math.round(_sellTotalValue) + "|" + Math.round(_sellPaidValue);
    }

    @Override
    public int compareTo(Partner partner) {
        Collator collator = Collator.getInstance(Locale.getDefault());
        return collator.compare(_id, partner.getId());
    }
}