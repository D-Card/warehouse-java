package ggc.exceptions;

public class NoSuchTransactionException extends Exception {

    private static final long serialVersionUID = 202110081026L;

    private final int _id;

    public NoSuchTransactionException(int id) {
        _id = id;
    }

    public int getId() {
        return _id;
    }
}