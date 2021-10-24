package ggc.exceptions;

public class NoSuchPartnerException extends Exception {

    private final String _id;

    public NoSuchPartnerException(String id) {
        _id = id;
    }

    public String getId() {
        return _id;
    }
}