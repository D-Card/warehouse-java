package ggc;

// FIXME import classes (cannot import from pt.tecnico or ggc.app)
import java.io.*;
import java.util.*;
import ggc.exceptions.*;

public class Mailbox implements Serializable{
    private List<Notification> _notifications = new ArrayList<Notification>();

    public List<Notification> listAllNotifications() { return _notifications; }
}