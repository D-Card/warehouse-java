package ggc;

// FIXME import classes (cannot import from pt.tecnico or ggc.app)
import java.io.*;
import java.util.*;
import ggc.exceptions.*;

public class Batch implements Serializable{

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
        return getProduct().getId() + "|" + getPartner().getName() + "|" + getPrice() + "|" + getStock();
    }
}
