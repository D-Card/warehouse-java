package ggc.transactions;
import java.io.*;
import java.util.*;
import ggc.partners.Partner;
import ggc.products.Product;

public abstract class Transaction implements Serializable {

    private static final long serialVersionUID = 202110270052L;

    private int _id;
    private Partner _partner;
    private Product _product;
    private int _amount;

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

}