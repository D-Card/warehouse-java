package ggc.transactions;

import java.io.*;
import java.util.*;
import ggc.partners.Partner;
import ggc.products.Product;

public class Breakdown extends Sale implements Serializable {

    private static final long serialVersionUID = 202111081638L;

    public Breakdown(int id, Partner partner, Product product, int amount, float baseValue, float realValue, int deadline) {
        super(id, partner, product, amount, baseValue, realValue, deadline);

    }



    @Override
    public String toString() {
        return "DESAGREGAÇÃO|" + getId() + "|" + getPartner().getId() + "|" + getProduct().getId() + "|" + getAmount() + "|" + getBaseValue() + "|" + getRealValue() + "|" + getPaidDate();
    }
}