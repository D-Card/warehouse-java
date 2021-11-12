package ggc.products;

import java.io.*;
import java.util.*;
import ggc.exceptions.*;

public class ProductSimple extends Product {

    private int _deadline = 5;

    public ProductSimple(String id) {
        setId(id);
    }

    public int getDeadline() { return _deadline; }

    public boolean enoughStock(int amount) { return (amount <= getStock()); }

    public void throwFirstMissingSimpleProduct(int amount) throws NotEnoughProductsException {
        if (amount <= getStock()) { return; }
        else { throw new NotEnoughProductsException(getId(), amount, getStock()); }
    }

    @Override
    public String toString() {
        return getId() + "|" + Math.round(getMaxPrice()) + "|" + getStock();
    }

}