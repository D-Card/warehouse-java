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
  private TreeMap<String, Partner> _partnerLookup = new TreeMap<String, Partner>();
  private LinkedList<Partner> _partners = new LinkedList<Partner>();

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

  public Product lookupProduct(String id) throws NoSuchProductException {
    if (!_productLookup.containsKey(id)) { throw new NoSuchProductException(id); }
    return _productLookup.get(id);
  }

  public Partner lookupPartner(String id) throws NoSuchPartnerException {
    if (!_partnerLookup.containsKey(id)) { throw new NoSuchPartnerException(id); }
    return _partnerLookup.get(id);
  }

  public LinkedList<Product> listAllProducts() {
    return _products;
  }

  public LinkedList<Batch> listAllBatches() {
    return _batches;
  }

  public LinkedList<Batch> listBatchesByPartner(Partner partner) {
    LinkedList<Batch> batchList = _batchesByPartner.get(partner);

    return batchList;
  }

  public LinkedList<Batch> listBatchesByProduct(Product product) {
    LinkedList<Batch> batchList = _batchesByProduct.get(product);

    return batchList;
  }

  public void registerNewPartner(String id, String name, String address) throws DuplicatePartnerException {
    try {
      lookupPartner(id);
    } catch (NoSuchPartnerException e) {
      Partner newPartner = new Partner(id, name, address);
      _partners.add(newPartner);
      _partnerLookup.put(id, newPartner);
      return;
    }

    throw new DuplicatePartnerException(id);
  }

  public LinkedList<Notification> listPartnerNotifications(Partner partner) {
    return partner.listAllNotifications();
  }

  public LinkedList<Partner> listAllPartners() {
    return _partners;
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
