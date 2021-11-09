package ggc.products;

import java.io.*;
import java.util.*;
import ggc.exceptions.*;

public class Recipe implements Serializable{

    private static final long serialVersionUID = 202110262344L;

    private List<Product> _products = new ArrayList<Product>();
    private Map<Product, Integer> _productQuantities = new HashMap<Product, Integer>();

    public void addProduct(Product product, int quantity) {
        _products.add(product);
        _productQuantities.put(product, quantity);
    }

    // Getters
    public List<Product> getProducts() {
        return _products;
    }


    public int getProductQuantity(Product product) {
        int quantity = _productQuantities.get(product);

        if (!_products.contains(product)) { return 0; }
        else { return quantity; }
    }

    @Override
    public String toString() {
        String currentString = "";
        int totalProducts = _products.size();
        Product currentProduct;

        for (int i = 0; i < totalProducts; i++) {
            currentProduct = _products.get(i);

            if (i > 0) { currentString += "#"; }
            currentString += currentProduct.getId() + ":" + getProductQuantity(currentProduct);
        }

        return currentString;
    }
}
