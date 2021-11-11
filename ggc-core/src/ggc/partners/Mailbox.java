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

    public void receiveNotification(Notification notification) {
        if (!checkIfProductBlocked(notification.getProduct())) {
            _notifications.add(notification);
        }

    }

    private void clearNotifications() { _notifications.clear(); }

    public ArrayList<Notification> listAllNotifications() {
        ArrayList<Notification> notifications = new ArrayList<Notification>(_notifications);
        clearNotifications();

        return notifications;
    }

    public ArrayList<Notification> listNotificationsByMethod(String method) {
        ArrayList<Notification> newNotifications = new ArrayList<Notification>();

        for (Notification n: _notifications) {
            if (n.getMethod().equals(method)) {
                newNotifications.add(n);
            }
        }

        // If listing by omission, clear all
        if (method.equals("")) { clearNotifications(); }

        return newNotifications;
    }

    public boolean checkIfProductBlocked(Product product) { return (_blockedProducts.contains(product)); }

}