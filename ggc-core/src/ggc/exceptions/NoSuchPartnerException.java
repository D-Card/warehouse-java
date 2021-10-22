package ggc.exceptions;

public class NoSuchPartnerException extends Exception {

    private final String _name;

    public NoSuchPartnerException(String name) {
        _name = name;
    }

    public String getName() {
        return _name;
    }
}