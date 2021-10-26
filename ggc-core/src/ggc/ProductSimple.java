package ggc;

// FIXME import classes (cannot import from pt.tecnico or ggc.app)
import java.io.*;
import java.util.*;
import ggc.exceptions.*;

public class ProductSimple extends Product {

    public ProductSimple(String id) {
        setId(id);
    }

    @Override
    public String toString() {
        return getId() + "|" + Math.round(getMaxPrice()) + "|" + getStock();
    }

}