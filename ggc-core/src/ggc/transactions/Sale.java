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
    private boolean _paid = false;

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
        int dayDif = _deadline - date;
        int n = getProduct().getDeadline();

        if (dayDif >= n) { // Updating the period in which the payment currently lies
            return 1;
        } else if (dayDif >= 0 && dayDif < n) {
            return 2;
        } else if (dayDif < 0 && dayDif >= n) {
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
        _paid = true;

        updateRealValue(date);

        Partner partner = getPartner();
        Status status = partner.getStatus();

        partner.setPoints(status.calculatePartnerPoints(partner, this));
    }

    @Override
    public String toString() {
        String text = "VENDA|" + getId() + "|" + getPartner().getId() + "|" + getProduct().getId() + "|" + getAmount() + "|" + Math.round(getBaseValue()) + "|" + Math.round(getRealValue()) + "|" + getLimitDate();
        if (getPaidDate() != -1) {
            text += "|" + getPaidDate();
        }
        return text;
    }
}