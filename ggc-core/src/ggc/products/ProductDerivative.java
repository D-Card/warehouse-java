package ggc.products;

import java.io.*;
import java.util.*;
import ggc.exceptions.*;

public class ProductDerivative extends Product {

    private Recipe _recipe;
    private float _multiplier;
    private int _deadline = 3;

    public ProductDerivative(String id, Recipe recipe, float multiplier) {
        setId(id);
        _recipe = recipe;
        _multiplier = multiplier;
    }

    // Getters
    public Recipe getRecipe() {
        return _recipe;
    }

    public float getMultiplier() {
        return _multiplier;
    }

    // Setters
    public void setRecipe(Recipe recipe) {
        _recipe = recipe;
    }

    public void setMultiplier(float multiplier) {
        _multiplier = multiplier;
    }

    public boolean enoughStock(int amount) {
        if (amount <= getStock()) return true;

        int amountNeeded = amount - getStock();

        for (Product p: _recipe.getProducts()) {
            if (!p.enoughStock(amountNeeded * _recipe.getProductQuantity(p))) { return false; }
        }

        return true;
    }

    public void throwFirstMissingSimpleProduct(int amount) throws NotEnoughProductsException {
        if (amount <= getStock()) return;

        for (Product p: _recipe.getProducts()) {
            p.throwFirstMissingSimpleProduct(amount * _recipe.getProductQuantity(p));
        }

        return;
    }


    @Override
    public String toString() {
        return getId() + "|" + Math.round(getMaxPrice()) + "|" + getStock() + "|" + _multiplier + "|" + _recipe.toString();
    }

}