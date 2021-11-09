package ggc.transactions;
import java.io.*;
import java.util.*;
import ggc.partners.Partner;
import ggc.products.Product;

public abstract class Status implements Serializable {

    private static final long serialVersionUID = 202110270052L;

    public abstract float calculateRealValue(float baseValue, int period, int dayDifference);

}