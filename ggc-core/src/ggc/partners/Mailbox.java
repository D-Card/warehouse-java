package ggc.partners;

import java.io.*;
import java.util.*;
import ggc.exceptions.*;

public class Mailbox implements Serializable{

    private static final long serialVersionUID = 202110262341L;

    private Set<Notification> _notifications = new TreeSet<Notification>();

    public Set<Notification> listAllNotifications() { return _notifications; }
}