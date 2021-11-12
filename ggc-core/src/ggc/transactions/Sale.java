package ggc.transactions;

import java.io.*;
import java.util.*;
import ggc.partners.Partner;
import ggc.products.Product;

public class Sale extends Transaction implements Serializable {

    private static final long serialVersionUID = 202111081613L;

    private float _baseValue;
    private float _realValue;
    private int _deadline;
    private int _paidDate = -1;

    public Sale(int id, Partner partner, Product product, int amount, float baseValue, float realValue, int deadline) {
        setId(id);
        setPartner(partner);
        setProduct(product);
        setAmount(amount);
        _baseValue = baseValue;
        _realValue = realValue;
        _deadline = deadline;
    }

    //Getters
    public float getBaseValue() {
        return _baseValue;
    }

    public float getRealValue() {
        return _realValue;
    }

    public int getLimitDate() {
        return _deadline;
    }

    public int getPaidDate() {
        return _paidDate;
    }

    public int getDeadline() { return _deadline; }

    //Setters
    public void setBaseValue(float baseValue) {
        _baseValue = baseValue;
    }

    public void setRealValue(float realValue) {
        _realValue = realValue;
    }

    public void setLimitDate(int deadline) {
        _deadline = deadline;
    }

    public void setPaidDate(int paidDate) {
        _paidDate = paidDate;
    }

    private int calculatePeriod(int date) {
        int n = getProduct().getDeadline();

        if (date <= _deadline - n) { // Updating the period in which the payment currently lies
            return 1;
        } else if (date <= _deadline) {
            return 2;
        } else if (date <= _deadline + n) {
            return 3;
        }

        return 4;
    }

    public void updateRealValue(int date) {
        int period = calculatePeriod(date);
        int dayDif = _deadline - date;

        _realValue = getPartner().getStatus().calculateRealValue(_baseValue, period, dayDif);
    }

    public void markAsPaid(int date) {
        _paidDate = date;
        setPaid();

        updateRealValue(date);
        getPartner().getStatus().updatePoints(this);
    }

    @Override
    public String toString() {
        String text = "VENDA|" + getId() + "|" + getPartner().getId() + "|" + getProduct().getId() + "|" + getAmount()
                + "|" + Math.round(getBaseValue()) + "|" + Math.round(getRealValue()) + "|" + getLimitDate();

        if (paid()) {
            return (text + "|" + getPaidDate());
        }

        return text;
    }
}