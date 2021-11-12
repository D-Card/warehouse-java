package ggc.partners;
import java.io.*;
import java.util.*;
import ggc.partners.Partner;
import ggc.products.Product;
import ggc.transactions.Transaction;

public class StatusSelection extends Status implements Serializable {

    private static final long serialVersionUID = 202111081425L;
    private static final String str = "SELECTION";

    public StatusSelection(Partner partner) {
        super(partner);
    }

    public StatusSelection(Partner partner, float points) {
        super(partner, points);
    }

    public float calculateRealValue(float baseValue, int period, int dayDifference) {
        switch (period) {
            case (1): // Period 1
                return (baseValue * 0.9f);
            case (2): // Period 2
                if (dayDifference >= 2) { return (baseValue * 0.95f); }
                else { return (baseValue); }
            case (3): // Period 3
                if (dayDifference > 1) { return (baseValue * (1 - dayDifference * 0.02f)); }
                else { return (baseValue); }
            case (4): // Period 4
                return (baseValue * (1 - dayDifference * 0.05f));
        }

        return (baseValue);
    }

    public void updatePoints(Transaction transaction) {
        if (transaction.getRealValue() >= 0) {
            float newPoints;

            if (transaction.getPaidDate() <= transaction.getDeadline() + 2) { // 2 day tolerance
                newPoints = (getPoints() + transaction.getRealValue() * 10);
            } else {
                newPoints = (getPoints() * 0.1f);
            }

            setPoints(newPoints);
        }
    }

    @Override
    public String toString() { return str; }
}