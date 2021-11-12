package ggc.partners;
import java.io.*;
import java.util.*;
import ggc.partners.Partner;
import ggc.products.Product;
import ggc.transactions.Transaction;

public class StatusElite extends Status implements Serializable {

    private static final long serialVersionUID = 202111081421L;
    private static final String str = "ELITE";

    public StatusElite(Partner partner) {
        super(partner);
    }

    public StatusElite(Partner partner, float points) {
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
                return (baseValue * 0.9f);
            case (3): // Period 3
                return (baseValue * 0.95f);
            case (4): // Period 4
                return (baseValue);
        }

        return (baseValue);
    }

    public void updatePoints(Transaction transaction) {
        if (transaction.getRealValue() >= 0) {
            float newPoints;

            if (transaction.getPaidDate() <= transaction.getDeadline() + 15) { // 15 day tolerance
                newPoints = (getPoints() + transaction.getRealValue() * 10);
            } else {
                newPoints = (getPoints() * 0.25f);
            }

            setPoints(newPoints);
        }
    }

    @Override
    public String toString() { return str; }
}