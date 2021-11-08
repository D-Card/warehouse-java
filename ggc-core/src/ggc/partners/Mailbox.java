package ggc.partners;

import java.io.*;
import java.util.*;
import ggc.exceptions.*;
import ggc.products.*;

public class Mailbox implements Serializable{

    private static final long serialVersionUID = 202110262341L;
    private List<Product> _blockedProducts = new ArrayList<Product>();
    private List<Notification> _notifications = new ArrayList<Notification>();

    public void toggleBlockedProduct(Product product) {
        if (!_blockedProducts.contains(product)) { _blockedProducts.add(product); }
        else { _blockedProducts.remove(product); }
    }

    public boolean checkIfProductBlocked(Product product) { return (_blockedProducts.contains(product)); }

    public List<Notification> listAllNotifications() { return _notifications; }
}