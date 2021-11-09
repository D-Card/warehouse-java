package ggc.transactions;
import java.io.*;
import java.util.*;
import ggc.partners.Partner;
import ggc.products.Product;

public class EliteStatus extends Status implements Serializable {

    private static final long serialVersionUID = 202110270052L;

    public float calculateRealValue(float baseValue, int period, int dayDifference) {
        switch (period) {
            case (1): // Period 1
                return (baseValue * 0.9f);
            case (2): // Period 2
                return (baseValue * 0.9f);
            case (3): // Period 3
                return (baseValue * 0.95f);
            case (4): // Period 4
                return (baseValue);
        }

        return (baseValue);
    }
}