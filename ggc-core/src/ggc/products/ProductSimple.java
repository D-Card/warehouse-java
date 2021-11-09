package ggc.products;

import java.io.*;
import java.util.*;
import ggc.exceptions.*;

public class ProductSimple extends Product {

    private int _deadline = 5;

    public ProductSimple(String id) {
        setId(id);
    }

    @Override
    public String toString() {
        return getId() + "|" + Math.round(getMaxPrice()) + "|" + getStock();
    }

}