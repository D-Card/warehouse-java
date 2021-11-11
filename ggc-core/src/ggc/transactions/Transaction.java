package ggc.transactions;
import java.io.*;
import java.util.*;
import ggc.partners.Partner;
import ggc.products.Product;

public abstract class Transaction implements Serializable, Comparable<Transaction> {

    private static final long serialVersionUID = 202110270052L;

    private int _id;
    private Partner _partner;
    private Product _product;
    private int _amount;
    private boolean _paid = true;

    // Getters
    public int getId() {
        return _id;
    }

    public Partner getPartner() {
        return _partner;
    }

    public Product getProduct() {
        return _product;
    }

    public int getAmount() {
        return _amount;
    }

    public int getPaidDate() { return -1; }

    public int getDeadline() { return -1; }

    // Setters
    public void setId(int id) {
        _id = id;
    }

    public void setPartner(Partner partner) {
        _partner = partner;
    }

    public void setProduct(Product product) {
        _product = product;
    }   

    public void setAmount(int amount) {
        _amount = amount;
    }

    public abstract float getRealValue();

    public abstract float getBaseValue();

    public void markAsPaid(int date) {};

    public void updateRealValue(int date) {};

    public boolean paid() { return _paid; }

    public int compareTo(Transaction transaction) {
        return (transaction.getId() - _id);
    }

}