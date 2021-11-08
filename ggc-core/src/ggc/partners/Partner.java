package ggc.partners;

import java.io.*;
import java.util.*;
import java.text.Collator;
import java.util.Locale;
import ggc.exceptions.*;

public class Partner implements Serializable, Comparable<Partner>{

    private static final long serialVersionUID = 202110262342L;

    private String _name;
    private String _id;
    private String _address;
    private String _status = "NORMAL";
    private int _points = 0;
    private float _buyTotalValue = 0;
    private float _sellTotalValue = 0;
    private float _sellPaidValue = 0;
    private Mailbox _mailbox = new Mailbox();

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

    public String getStatus() {
        return _status;
    }

    public int getPoints() {
        return _points;
    }

    public Mailbox getMailbox() {
        return _mailbox;
    }

    public Set<Notification> listAllNotifications() { return _mailbox.listAllNotifications(); }

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

    public void setStatus(String status) {
        _status = status;
    }

    public void setPoints(int points) {
        _points = points;
    }

    public void setMailbox(Mailbox mailbox) {
        _mailbox = mailbox;
    }

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