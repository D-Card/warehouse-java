package ggc.exceptions;

public class NoSuchProductException extends Exception {

    private static final long serialVersionUID = 202110081026L;

    private final String _id;

    public NoSuchProductException(String id) {
        _id = id;
    }

    public String getId() {
        return _id;
    }
}