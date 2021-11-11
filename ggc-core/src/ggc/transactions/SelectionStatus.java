package ggc.transactions;
import java.io.*;
import java.util.*;
import ggc.partners.Partner;
import ggc.products.Product;

public class SelectionStatus extends Status implements Serializable {

    private static final long serialVersionUID = 202111081425L;
    private static final String str = "SELECTION";

    public float calculateRealValue(float baseValue, int period, int dayDifference) {
        switch (period) {
            case (1): // Period 1
                return (baseValue * 0.9f);
            case (2): // Period 2
                if (dayDifference >= 2) { return (baseValue * 0.95f); }
                else { return (baseValue); }
            case (3): // Period 3
                if (dayDifference > 1) { return (baseValue * (1 + dayDifference * 0.02f)); }
                else { return (baseValue); }
            case (4): // Period 4
                return (baseValue * (1 + dayDifference * 0.05f));
        }

        return (baseValue);
    }

    public float calculatePartnerPoints(Partner partner, Transaction transaction) {
        if (transaction.getPaidDate() <= transaction.getDeadline() + 2) { // 2 day tolerance
            return (partner.getPoints() + transaction.getRealValue() * 10);
        } else {
            return (partner.getPoints() * 0.1f);
        }
    }

    @Override
    public String toString() { return str; }
}