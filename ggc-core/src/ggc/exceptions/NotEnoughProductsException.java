package ggc.exceptions;

public class NotEnoughProductsException extends Exception {

    private static final long serialVersionUID = 202110081026L;
    private int _currentStock = 0;

    public NotEnoughProductsException() {}

    public NotEnoughProductsException(int currentStock) {
        _currentStock = currentStock;
    }

    public int getCurrentStock() { return _currentStock; }

}