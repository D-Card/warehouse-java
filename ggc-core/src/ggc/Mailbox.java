package ggc;

import java.io.*;
import java.util.*;
import ggc.exceptions.*;

public class Mailbox implements Serializable{

    private static final long serialVersionUID = 202110262341L;

    private List<Notification> _notifications = new ArrayList<Notification>();

    public List<Notification> listAllNotifications() { return _notifications; }
}