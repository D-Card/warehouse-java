package ggc.transactions;

import java.io.*;
import java.util.*;
import ggc.partners.Partner;
import ggc.products.Product;

public class Sale extends Transaction implements Serializable {

    private static final long serialVersionUID = 202111081613L;

    private float _baseValue;
    private float _realValue;
    private int _limitDate;
    private int _paidDate = -1;

    public Sale(int id, Partner partner, Product product, int amount, float baseValue, float realValue, int limitDate) {
        setId(id);
        setPartner(partner);
        setProduct(product);
        setAmount(amount);
        _baseValue = baseValue;
        _realValue = realValue;
        _limitDate = limitDate;
    }

    //Getters
    public float getBaseValue() {
        return _baseValue;
    }

    public float getRealValue() {
        return _realValue;
    }

    public int getLimitDate() {
        return _limitDate;
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

    public void setLimitDate(int limitDate) {
        _limitDate = limitDate;
    }

    public void setPaidDate(int paidDate) {
        _paidDate = paidDate;
    }

    @Override
    public String toString() {
        String text = "VENDA|" + getId() + "|" + getPartner().getId() + "|" + getProduct().getId() + "|" + getAmount() + "|" + getBaseValue() + "|" + getRealValue() + "|" + getLimitDate();
        if (getPaidDate() != -1) {
            text += "|" + getPaidDate();
        }
        return text;
    }
}