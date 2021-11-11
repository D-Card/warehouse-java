package ggc.products;

import java.io.*;
import java.util.*;
import ggc.exceptions.*;

public class Receipt implements Serializable{

    private static final long serialVersionUID = 202110282344L;

    private Map<Product, Float> _productPrices = new HashMap<Product, Float>();
    private Recipe _recipe;
    private int _quantity;

    public Receipt(Recipe recipe, int quantity) { // Has a recipe associated and how many times that recipe was used
        _recipe = recipe;
        _quantity = quantity;
    }

    public void productSetPrice(Product product, float price) { // Each sum of products has a price associated with it
        _productPrices.put(product, price);
    }

    public String toString() {
        String currentString = "";

        for (Product p: _recipe.getProducts()) {
            currentString += (p.getId() + ":" + _recipe.getProductQuantity(p) * _quantity + ":" + Math.round(_recipe.getProductQuantity(p) * _productPrices.get(p) * _quantity)) + "#";
        }

        return currentString.substring(0, currentString.length()-1); // Remove last #
    }
}
