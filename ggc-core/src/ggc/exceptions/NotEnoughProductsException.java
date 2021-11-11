package ggc.exceptions;

public class NotEnoughProductsException extends Exception {

    private static final long serialVersionUID = 202110081036L;
    private int _currentStock = 0;
    private int _stockRecquired = 0;
    private String _product = "";

    
    public NotEnoughProductsException(String p, int stockRecquired, int currentStock) {
        _currentStock = currentStock;
        _stockRecquired = stockRecquired;
        _product = p;
    }

    public int getCurrentStock() { return _currentStock; }

    public int getStockRecquired() { return _stockRecquired; }

    public String getProduct() { return _product; }

}