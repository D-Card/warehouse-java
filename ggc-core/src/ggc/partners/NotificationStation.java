package ggc.partners;

import java.io.*;
import java.util.*;
import ggc.exceptions.*;
import ggc.products.*;


public class NotificationStation implements Serializable{

    private static final long serialVersionUID = 202110262316L;

    private ArrayList<Mailbox> _mailboxes = new ArrayList<Mailbox>();

    public void addMailbox(Mailbox mailbox) { _mailboxes.add(mailbox); }

    public void emitNotification(Notification notification) {
        for (Mailbox m: _mailboxes) {
            m.receiveNotification(notification);
        }
    }

}