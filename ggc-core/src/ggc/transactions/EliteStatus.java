package ggc.transactions;
import java.io.*;
import java.util.*;
import ggc.partners.Partner;
import ggc.products.Product;

public class EliteStatus extends Status implements Serializable {

    private static final long serialVersionUID = 202111081421L;
    private static final String str = "ELITE";

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

    public float calculatePartnerPoints(Partner partner, Transaction transaction) {
        if (transaction.getPaidDate() <= transaction.getDeadline() + 15) { // 15 day tolerance
            return (partner.getPoints() + transaction.getRealValue() * 10);
        } else {
            return (partner.getPoints() * 0.25f);
        }
    }

    @Override
    public String toString() { return str; }
}