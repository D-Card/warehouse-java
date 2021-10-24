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
  private HashMap<String, Product> _productLookup = new HashMap<String, Product>();
  private LinkedList<Batch> _batches = new LinkedList<Batch>();
  private HashMap<Partner, LinkedList<Batch>> _batchesByPartner = new HashMap<Partner, LinkedList<Batch>>();
  private HashMap<Product, LinkedList<Batch>> _batchesByProduct = new HashMap<Product, LinkedList<Batch>>();
  private HashMap<String, Partner> _partnerLookup = new HashMap<String, Partner>();
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
      _batchesByPartner.put(newPartner, new LinkedList<Batch>());
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
      _productLookup.put(id, product);
      _batchesByProduct.put(product, new LinkedList<Batch>());
      _products.add(product);
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
      _productLookup.put(id, product);
      _batchesByProduct.put(product, new LinkedList<Batch>());
      _products.add(product);
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
          String[] fields = line.split("\\|");
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
              String id = fields[1];
              String partnerId = fields[2];
              float price = Float.parseFloat(fields[3]);
              int stock = Integer.parseInt(fields[4]);
              float multiplier = Float.parseFloat(fields[5]);

              String[] recipeStrings = fields[6].split("#");
              Recipe recipe = new Recipe();

              for(int i = 0; i < recipeStrings.length; i++) {
                String[] ss = recipeStrings[i].split(":");
                try {
                  recipe.addProduct(lookupProduct(ss[0]), Integer.parseInt(ss[1]));
                } catch (NoSuchProductException e) {};
              }

              DerivativeProduct product = registerProductDerivative(id, recipe, multiplier, price, stock);
              try {
                Partner partner = lookupPartner(partnerId);
                registerNewBatch((Product) product, partner, price, stock);
              } catch (NoSuchPartnerException e) {}

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
