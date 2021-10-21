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
  private TreeMap<String, Product> _productLookup = new TreeMap<String, Product>();
  private LinkedList<Batch> _batches = new LinkedList<Batch>();
  private TreeMap<Partner, LinkedList<Batch>> _batchesByPartner = new TreeMap<Partner, LinkedList<Batch>>();
  private TreeMap<Product, LinkedList<Batch>> _batchesByProduct = new TreeMap<Product, LinkedList<Batch>>();

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

  public Product lookupProduct(String id) {
    return _productLookup.get(id);
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

  public void listBatchesByPartner(Partner partner) {
    LinkedList<Batch> batchList = _batchesByPartner.get(partner);

    int totalBatches = batchList.size();
    Batch currentBatch;

    for (int i = 0; i < totalBatches; i++) {
      currentBatch = _batches.get(i);

      System.out.println(currentBatch);
    }
  }

  public void listBatchesByProduct(Product product) {
    LinkedList<Batch> batchList = _batchesByProduct.get(product);

    int totalBatches = batchList.size();
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
