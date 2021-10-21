package ggc;

// FIXME import classes (cannot import from pt.tecnico or ggc.app)
import java.io.*;
import java.util.*;
import ggc.exceptions.*;

/**
 * Class Warehouse implements a warehouse.
 */
public class Warehouse implements Serializable {

  /** Serial number for serialization. */
  private static final long serialVersionUID = 202109192006L;

  // FIXME define attributes
  private double _availableBalance = 0;
  private double _contabilisticBalance = 0;
  private int _date = 0;
  private LinkedList<Product> _products = new LinkedList<Product>();
  private LinkedList<Batch> _batches = new LinkedList<Batch>();

  // FIXME define constructor(s)
  // FIXME define methods

  public void advanceDays(int days) throws NoSuchDateException {
    if (days > 0) 
      _date += days;
    else throw new NoSuchDateException(days);
  }

  public int getDate() {
    return _date;
  }

  public double getAvailableBalance() {
    return _availableBalance;
  }

  public double getContabilisticBalance() {
    return _contabilisticBalance;
  }

  public void listAllProducts() {
    int totalProducts = _products.size();
    Product currentProduct;

    for (int i = 0; i < totalProducts; i++) {
      currentProduct = _products.get(i);

      System.out.println(currentProduct);
    }
  }

  public void listAllBatches() {
    int totalBatches = _batches.size();
    Batch currentBatch;

    for (int i = 0; i < totalBatches; i++) {
      currentBatch = _batches.get(i);

      System.out.println(currentBatch);
    }
  }

  /**
   * @param txtfile filename to be loaded.
   * @throws IOException
   * @throws BadEntryException
   */
  void importFile(String txtfile) throws IOException, BadEntryException /* FIXME maybe other exceptions */ {
    //FIXME implement method
  }

}
