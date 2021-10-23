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

  public void advanceDate(int days) throws NoSuchDateException {
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

  public SimpleProduct registerProductSimple(String id, float price, int stock) {
    SimpleProduct product;

    try {
      product = (SimpleProduct) lookupProduct(id);
    } catch (NoSuchProductException e) {
      product = new SimpleProduct(id);
    }

    product.addStock(stock);
    if (product.getMaxPrice() < price) { product.setMaxPrice(price); }

    return product;
  }

  public DerivativeProduct registerProductDerivative(String id, Recipe recipe, float multiplier, float price, int stock) {
    DerivativeProduct product;

    try {
      product = (DerivativeProduct) lookupProduct(id);
    } catch (NoSuchProductException e) {
      product = new DerivativeProduct(id, recipe, multiplier);
    }

    product.addStock(stock);
    if (product.getMaxPrice() < price) { product.setMaxPrice(price); }

    return product;
  }

  public void registerNewBatch(Product product, Partner partner, float price, int stock) {
    Batch batch = new Batch(product, partner, price, stock);

    _batches.add(batch);
    _batchesByPartner.get(partner).add(batch);
    _batchesByProduct.get(product).add(batch);
  }

  /**
   * @param txtfile filename to be loaded.
   * @throws IOException
   * @throws BadEntryException
   */
  public void importFile(String txtfile) throws IOException, BadEntryException /* FIXME maybe other exceptions */ {

      try (BufferedReader in = new BufferedReader(new FileReader(txtfile))) {
        String s;
        while ((s = in.readLine()) != null) {
          String line = new String(s.getBytes(), "UTF-8");

          String[] fields = line.split("|");
          switch (fields[0]) {
            case "PARTNER" -> {
              try {registerNewPartner(fields[1], fields[2], fields[3]); }
              catch (DuplicatePartnerException e) {}
            }
            case "BATCH_S" -> {
              String id = fields[1];
              String partnerId = fields[2];
              float price = Float.parseFloat(fields[3]);
              int stock = Integer.parseInt(fields[4]);

              SimpleProduct product = registerProductSimple(id, price, stock);
              try {
                Partner partner = lookupPartner(partnerId);
                registerNewBatch(product, partner, price, stock);
              } catch (NoSuchPartnerException e) {}


            }
            case "BATCH_M" -> {
              /*
              String[] recipeStrings = fields[6].split("#");
              Recipe recipe = new Recipe();

              for(int i = 0; i < recipeStrings.size(); i++) {
                String[] s = recipeStrings[i].split(":");
                recipe.addProduct(lookupProduct(s[0]), s[1]);
              }

              DerivativeProduct product = registerProductDerivative(fields[1], recipe, field[5], fields[3], fields[4]);
              Partner partner = lookupPartner(fields[2]);
              registerNewBatch((Product) product, partner, fields[3], fields[4]);
              */
            }
            default -> throw new BadEntryException(fields[0]);
          }
        }
      } catch (FileNotFoundException e) {
        e.printStackTrace();
      } catch (IOException e) {
        e.printStackTrace();
      } catch (BadEntryException e) {
        e.printStackTrace();
      }
    }

}
