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
  private List<Product> _products = new ArrayList<Product>();
  private Map<String, Product> _productLookup = new TreeMap<String, Product>(String.CASE_INSENSITIVE_ORDER);
  private List<Batch> _batches = new ArrayList<Batch>();
  private Map<Partner, ArrayList<Batch>> _batchesByPartner = new HashMap<Partner, ArrayList<Batch>>();
  private Map<Product, ArrayList<Batch>> _batchesByProduct = new HashMap<Product, ArrayList<Batch>>();
  private Map<String, Partner> _partnerLookup = new TreeMap<String, Partner>(String.CASE_INSENSITIVE_ORDER);
  private List<Partner> _partners = new ArrayList<Partner>();

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

  public List<Product> listAllProducts() {
    _products.sort(null);
    return _products;
  }

  public List<Batch> listAllBatches() {
    _batches.sort(null);
    return _batches;
  }

  public List<Batch> listBatchesByPartner(Partner partner) {
    List<Batch> batchList = _batchesByPartner.get(partner);
    batchList.sort(null);
    return batchList;
  }

  public List<Batch> listBatchesByProduct(Product product) {
    List<Batch> batchList = _batchesByProduct.get(product);
    batchList.sort(null);
    return batchList;
  }

  public void registerNewPartner(String id, String name, String address) throws DuplicatePartnerException {
    try {
      lookupPartner(id);
    } catch (NoSuchPartnerException e) {
      Partner newPartner = new Partner(id, name, address);
      _partners.add(newPartner);
      _partnerLookup.put(id, newPartner);
      _batchesByPartner.put(newPartner, new ArrayList<Batch>());
      return;
    }

    throw new DuplicatePartnerException(id);
  }

  public List<Notification> listPartnerNotifications(Partner partner) {
    return partner.listAllNotifications();
  }

  public List<Partner> listAllPartners() {
    _partners.sort(null);
    return _partners;
  }

  public SimpleProduct registerProductSimple(String id, float price, int stock) {
    SimpleProduct product;

    try {
      product = (SimpleProduct) lookupProduct(id);
    } catch (NoSuchProductException e) {
      product = new SimpleProduct(id);
      _productLookup.put(id, product);
      _batchesByProduct.put(product, new ArrayList<Batch>());
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
      _batchesByProduct.put(product, new ArrayList<Batch>());
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
  public void importFile(String txtfile) throws IOException, BadEntryException, DuplicatePartnerException, NoSuchPartnerException, NoSuchProductException, BadEntryException {
    BufferedReader in = new BufferedReader(new FileReader(txtfile));
    String s;
    while ((s = in.readLine()) != null) {
      String line = new String(s.getBytes(), "UTF-8");
      String[] fields = line.split("\\|");
      switch (fields[0]) {
        case "PARTNER" -> {
          registerNewPartner(fields[1], fields[2], fields[3]);
        }

        case "BATCH_S" -> {
          String id = fields[1];
          String partnerId = fields[2];
          float price = Float.parseFloat(fields[3]);
          int stock = Integer.parseInt(fields[4]);

          SimpleProduct product = registerProductSimple(id, price, stock);
          Partner partner = lookupPartner(partnerId);
          registerNewBatch(product, partner, price, stock);

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
            recipe.addProduct(lookupProduct(ss[0]), Integer.parseInt(ss[1]));
          }

          DerivativeProduct product = registerProductDerivative(id, recipe, multiplier, price, stock);
          Partner partner = lookupPartner(partnerId);
          registerNewBatch((Product) product, partner, price, stock);
        }
        default -> throw new BadEntryException(fields[0]);
        }
      }
  }
}
