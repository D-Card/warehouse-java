    package ggc.products;

import java.io.*;
import java.util.*;
import java.text.Collator;
import java.util.Locale;
import ggc.exceptions.*;
import ggc.products.*;

public abstract class Product implements Serializable, Comparable<Product>{

    private static final long serialVersionUID = 202110262343L;

    private String _id;
    private float _maxPrice = 0;
    private int _stock = 0;
    private PriorityQueue<Batch> _batches = new PriorityQueue<Batch>();

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

    public Recipe getRecipe() { return null; }

    public PriorityQueue<Batch> getBatches() { return _batches; }

    public abstract int getDeadline();

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

    public abstract boolean enoughStock(int amount);

    public void addStock(int stock) {
        _stock += stock;
    }

    public void addBatch(Batch batch) { _batches.add(batch); }

    public void removeBatch(Batch batch) { _batches.remove(batch); }

    public abstract void throwFirstMissingSimpleProduct(int amount) throws NotEnoughProductsException;

    @Override
    public int compareTo(Product product) {
        Collator collator = Collator.getInstance(Locale.getDefault());
        return collator.compare(_id, product.getId());
    }
}