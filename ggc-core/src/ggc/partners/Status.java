package ggc.partners;
import java.io.*;
import java.util.*;
import ggc.partners.Partner;
import ggc.products.Product;
import ggc.transactions.Transaction;

public abstract class Status implements Serializable {

    private static final long serialVersionUID = 202111081415L;
    private Partner _partner;
    private float _points;

    public Status(Partner partner) {
        _partner = partner;
        _points = 0;
    }

    public Status(Partner partner, float points) {
        _partner = partner;
        _points = points;
    }

    public float getPoints() {
        return _points;
    }

    public void setPoints(float points) {
        _points = points;

        if (_points >= 25000) { // If score >= 25000, new status is ELITE
            _partner.setStatus(new StatusElite(_partner, points));
        } else if (_points >= 2000) { // If score >= 2000, new status is SELECTION
            _partner.setStatus(new StatusSelection(_partner, points));
        } else { // Else, new status is NORMAL
            _partner.setStatus(new StatusNormal(_partner, points));
        }
    }

    public void addPoints(float points) {
        setPoints(getPoints() + points);
    }

    public Partner getPartner() {
        return _partner;
    }

    public abstract float calculateRealValue(float baseValue, int period, int dayDifference);

    public abstract void updatePoints(Transaction transaction);

}