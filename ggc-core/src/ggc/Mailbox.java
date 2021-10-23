package ggc;

// FIXME import classes (cannot import from pt.tecnico or ggc.app)
import java.io.*;
import java.util.*;
import ggc.exceptions.*;

public class Mailbox implements Serializable{
    private LinkedList<Notification> _notifications = new LinkedList<Notification>();

    public LinkedList<Notification> listAllNotifications() { return _notifications; }
}