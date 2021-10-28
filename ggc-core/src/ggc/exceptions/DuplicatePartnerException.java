package ggc.exceptions;

public class DuplicatePartnerException extends Exception {

    private static final long serialVersionUID = 202110081023L;
    
    private final String _id;

    public DuplicatePartnerException(String id) {
        _id = id;
    }

    public String getId() {
        return _id;
    }
}