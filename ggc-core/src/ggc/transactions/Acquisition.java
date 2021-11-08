package ggc.transactions;

import java.io.*;
import java.util.*;
import ggc.partners.Partner;
import ggc.products.Product;

public class Acquisition extends Transaction implements Serializable {

    private static final long serialVersionUID = 202111081638L;

    private float _paidValue;
    private int _paidDate;

    public Acquisition(int id, Partner partner, Product product, int amount, float paidValue, int paidDate) {
        setId(id);
        setPartner(partner);
        setProduct(product);
        setAmount(amount);
        _paidValue = paidValue;
        _paidDate = paidDate;
    }

    //Getters
    public float getPaidValue() {
        return _paidValue;
    }

    public int getPaidDate() {
        return _paidDate;
    }
    //Setters
    public void setPaidValue(float paidValue) {
        _paidValue = paidValue;
    }

    public void setPaidDate(int paidDate) {
        _paidDate = paidDate;
    }

    @Override
    public String toString() {
        return "COMPRA|" + getId() + "|" + getPartner().getId() + "|" + getProduct().getId() + "|" + getAmount() + "|" + getPaidValue() + "|" + getPaidDate();
    }
}