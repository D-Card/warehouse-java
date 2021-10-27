package ggc;

import java.io.*;
import java.util.*;
import java.text.Collator;
import java.util.Locale;
import ggc.exceptions.*;

public abstract class Product implements Serializable, Comparable<Product>{

    private static final long serialVersionUID = 202110262343L;

    private String _id;
    private float _maxPrice = 0;
    private int _stock = 0;

    // Getters
    public String getId() {
        return _id;
    }

    public float getMaxPrice() {
        return _maxPrice;
    }

    public int getStock() {
        return _stock;
    }


    // Setters
    public void setId(String id) {
        _id = id;
    }

    public void setMaxPrice(float maxPrice) {
        _maxPrice = maxPrice;
    }

    public void setStock(int stock) {
        _stock = stock;
    }

    public void addStock(int stock) {
        _stock += stock;
    }

    @Override
    public int compareTo(Product product) {
        return Collator.getInstance(Locale.getDefault()).compare(_id, product.getId());
    }
}