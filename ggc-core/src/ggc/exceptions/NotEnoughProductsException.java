package ggc.exceptions;

public class NotEnoughProductsException extends Exception {

    private static final long serialVersionUID = 202110081036L;
    private int _currentStock = 0;
    private int _stockNeeded = 0;
    private String _productStringAsked = "";

    public NotEnoughProductsException() {}

    public NotEnoughProductsException(int currentStock) {
        _currentStock = currentStock;
    }

    public NotEnoughProductsException(String p, int stockNeeded, int currentStock) {
        _currentStock = currentStock;
        _stockNeeded = stockNeeded;
        _productStringAsked = p;
    }

    public int getCurrentStock() { return _currentStock; }

    public int getStockNeeded() { return _stockNeeded; }

    public String getProduct() { return _productStringAsked; }

}