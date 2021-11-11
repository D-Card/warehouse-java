package ggc.transactions;
import java.io.*;
import java.util.*;
import ggc.partners.Partner;
import ggc.products.Product;

public abstract class Status implements Serializable {

    private static final long serialVersionUID = 202111081415L;

    public abstract float calculateRealValue(float baseValue, int period, int dayDifference);

    public abstract float calculatePartnerPoints(Partner partner, Transaction transaction);

}