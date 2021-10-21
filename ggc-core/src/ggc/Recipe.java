package ggc;

// FIXME import classes (cannot import from pt.tecnico or ggc.app)
import java.io.*;
import java.util.*;
import ggc.exceptions.*;

public class Recipe {

    private LinkedList<Product> _products = new LinkedList<Product>;
    private TreeMap<Product, int> _productQuantities = new TreeMap<Product, int>;

    public void addProduct(Product product, int quantity) {
        _products.add(product);
        _productQuantities.put(product, quantity);
    }

    // Getters
    public LinkedList<Product> getProducts() {
        return _products;
    }

    public int getProductQuantity(Product product) {
        int quantity = _productQuantities.get(product);

        if quantity == null return 0;
        else return quantity;
    }

    @Override
    public String toString() {
        String currentString = "";
        int totalProducts = _products.size();
        Product currentProduct;

        for (i = 0; i < totalProducts; i++) {
            currentProduct = _products.get(i);

            if i > 0 currentString += "#";
            currentString += currentProduct.getName() + "-" + getProductQuantity(currentProduct);
        }

    }
}
