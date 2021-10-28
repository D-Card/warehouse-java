package ggc.exceptions;

public class NoSuchDateException extends Exception {

    private static final long serialVersionUID = 202110081024L;

    private final int _days;
    
    public NoSuchDateException(int days){
        _days = days;
    }

    public int getDate() {
        return _days;
    }
}