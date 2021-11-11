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
    private Status _status = new StatusNormal(this);
    private Mailbox _mailbox = new Mailbox();
    private PriorityQueue<Batch> _batches = new PriorityQueue<Batch>();

    private ArrayList<Transaction> _sales = new ArrayList<Transaction>();
    private ArrayList<Transaction> _acquisitions = new ArrayList<Transaction>();
    private ArrayList<Transaction> _breakdowns = new ArrayList<Transaction>();

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
        return _status.getPoints();
    }

    public Mailbox getMailbox() {
        return _mailbox;
    }

    public List<Notification> listAllNotifications() { return _mailbox.listAllNotifications(); }

    public List<Notification> listAllNotificationsByMethod(String method) { return _mailbox.listNotificationsByMethod(method); }

    public PriorityQueue<Batch> getBatches() { return _batches; }

    public ArrayList<Transaction> getSales() {
        ArrayList<Transaction> sales = new ArrayList<Transaction>(_sales); // Create a shallow copy
        sales.addAll(_breakdowns);
        sales.sort(null);

        return sales;
    }

    public ArrayList<Transaction> getAcquisitions() { return _acquisitions; }

    public ArrayList<Transaction> getPaidSales() {
        ArrayList<Transaction> paidSales = new ArrayList<Transaction>();

        for (Transaction t: _sales) {
            if (t.paid()) {
                paidSales.add(t);
            }
        }

        return paidSales;
    }

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


    public void setMailbox(Mailbox mailbox) {
        _mailbox = mailbox;
    }

    public void addBatch(Batch batch) { _batches.add(batch); }

    public void removeBatch(Batch batch) { _batches.remove(batch); }

    public void addSale(Transaction sale) {
        _sales.add(sale);
    }

    public void addBreakdown(Transaction breakdown) {
        _breakdowns.add(breakdown);
    }

    public float getTotalSellValue() {
        float value = 0;

        for (Transaction t: _sales) {
            value += t.getBaseValue();
        }

        return value;
    }

    public float getTotalPaidValue() {
        float value = 0;

        for (Transaction t: _sales) {
            value += t.getRealValue();
        }

        return value;
    }

    public void addAcquisition(Transaction acquisition) { _acquisitions.add(acquisition); }

    public float getTotalBuyValue() {
        float value = 0;

        for (Transaction t: _acquisitions) {
            value += t.getRealValue() * t.getAmount();
        }

        return value;
    }

    @Override
    public String toString() {
        return _id + "|" + _name + "|" + _address + "|" + _status + "|" + Math.round(getPoints()) + "|" + Math.round(getTotalBuyValue()) + "|" + Math.round(getTotalSellValue()) + "|" + Math.round(getTotalPaidValue());
    }

    @Override
    public int compareTo(Partner partner) {
        Collator collator = Collator.getInstance(Locale.getDefault());
        return collator.compare(_id, partner.getId());
    }
}