package ggc.partners;
import java.io.*;
import java.util.*;
import ggc.partners.Partner;
import ggc.products.Product;
import ggc.transactions.Transaction;

public class StatusNormal extends Status implements Serializable {

    private static final long serialVersionUID = 202111081418L;
    private static final String str = "NORMAL";

    public StatusNormal(Partner partner) {
        super(partner);
    }

    public StatusNormal(Partner partner, float points) {
        super(partner, points);
    }

    public float calculateRealValue(float baseValue, int period, int dayDifference) {
        if (baseValue < 0) {
            return 0;
        }
        switch (period) {
            case (1): // Period 1
                return (baseValue * 0.9f);
            case (2): // Period 2
                return (baseValue);
            case (3): // Period 3
                return (baseValue * (1 - dayDifference * 0.05f));
            case (4): // Period 4
                return (baseValue * (1 - dayDifference * 0.1f));
        }

        return (baseValue);
    }

    public void updatePoints(Transaction transaction) {
        if (transaction.getRealValue() >= 0) {
            float newPoints;

            if (transaction.getPaidDate() <= transaction.getDeadline()) {
                newPoints = (getPoints() + transaction.getRealValue() * 10);
            } else {
                newPoints = 0;
            }

            setPoints(newPoints);
        }
    }

    @Override
    public String toString() { return str; }
}