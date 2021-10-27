package ggc;

import java.io.*;
import java.util.*;
import java.text.Collator;
import java.util.Locale;
import ggc.exceptions.*;

public class Batch implements Serializable, Comparable<Batch>{

    private static final long serialVersionUID = 202110262339L;

    private float _price;
    private Product _product;
    private int _stock;
    private Partner _partner;

    public Batch(Product product, Partner partner, float price, int stock) {
        _price = price;
        _stock = stock;
        _product = product;
        _partner = partner;
    }

    // Getters
    public float getPrice() {
        return _price;
    }
    public Product getProduct() {
        return _product;
    }

    public int getStock() {
        return _stock;
    }

    public Partner getPartner() {
        return _partner;
    }

    // Setters
    public void setPrice(float price) {
        _price = price;
    }

    public void setProduct(Product product) {
        _product = product;
    }

    public void setStock(int stock) {
        _stock = stock;
    }

    public void setPartner(Partner partner) {
        _partner = partner;
    }

    public void addStock(int stock) {
        _stock += stock;
    }

    @Override
    public String toString() {
        return getProduct().getId() + "|" + getPartner().getId() + "|" + Math.round(getPrice()) + "|" + getStock();
    }

    @Override
    public int compareTo(Batch batch) {
        int signProduct = Collator.getInstance(Locale.getDefault()).compare(_product.getId(), batch.getProduct().getId());
        int signPartner = Collator.getInstance(Locale.getDefault()).compare(_partner.getId(), batch.getPartner().getId());
        if (signProduct != 0) return signProduct;
        if (signPartner != 0) return signPartner;
        if (_price != batch.getPrice()) return (int)(_price - batch.getPrice());
        return 0;
    }
}
