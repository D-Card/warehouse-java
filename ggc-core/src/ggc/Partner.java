package ggc;

// FIXME import classes (cannot import from pt.tecnico or ggc.app)
import java.io.*;
import java.util.*;
import ggc.exceptions.*;

public class Partner {

    private String _name;
    private String _address;
    private int _status;
    private int _points;
    private Mailbox _mailbox = new Mailbox();
    // TBD - Datastructure for transaction history

    public Partner(String name, String address) {
        _name = name;
        _address = address;
    }


    // Getters
    public String getName() {
        return _name;
    }

    public String getAddress() {
        return _address;
    }

    public int getStatus() {
        return _status;
    }

    public int getPoints() {
        return _points;
    }

    public Mailbox getMailbox() {
        return _mailbox;
    }

    // Setters
    public void setName(String name) {
        _name = name;
    }

    public void setAddress(String address) {
        _address = address;
    }

    public void setStatus(int status) {
        _status = status;
    }

    public void setPoints(int points) {
        _points = points;
    }

    public void setMailbox(Mailbox mailbox) {
        _mailbox = mailbox;
    }

}