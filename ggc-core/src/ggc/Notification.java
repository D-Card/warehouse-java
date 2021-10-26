package ggc;

// FIXME import classes (cannot import from pt.tecnico or ggc.app)
import java.io.*;
import java.util.*;
import ggc.exceptions.*;

public class Notification implements Serializable{

    private static final long serialVersionUID = 202110262346L;

    private String _type;
    private Product _product;
    private float _price;

    public Notification(String type, Product product, float price) {
        _type = type;
        _product = product;
        _price = price;
    }

    @Override
    public String toString() {
        return _type + "|" + _product + "|" + _price;
    }
}