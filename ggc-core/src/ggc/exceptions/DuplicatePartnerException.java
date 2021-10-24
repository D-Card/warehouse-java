package ggc.exceptions;

public class DuplicatePartnerException extends Exception {

    private final String _id;

    public DuplicatePartnerException(String id) {
        _id = id;
    }

    public String getId() {
        return _id;
    }
}