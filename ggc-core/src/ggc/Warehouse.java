package ggc;


import java.io.*;
import java.util.*;
import ggc.exceptions.*;
import ggc.partners.*;
import ggc.products.*;
import ggc.transactions.*;

/**
 * Class Warehouse implements a warehouse.
 */
public class Warehouse implements Serializable {

  /**
   * Serial number for serialization.
   */
  private static final long serialVersionUID = 202109192006L;

  // TODO - javadocs
  private double _availableBalance = 0;
  private double _contabilisticBalance = 0;
  private int _date = 0;
  private Set<Product> _products = new TreeSet<Product>();
  private Map<String, Product> _productLookup = new TreeMap<String, Product>(String.CASE_INSENSITIVE_ORDER);
  private Set<Batch> _batches = new TreeSet<Batch>();
  private Map<Partner, TreeSet<Batch>> _batchesByPartner = new HashMap<Partner, TreeSet<Batch>>();
  private Map<Product, TreeSet<Batch>> _batchesByProduct = new HashMap<Product, TreeSet<Batch>>();
  private Map<String, Partner> _partnerLookup = new TreeMap<String, Partner>(String.CASE_INSENSITIVE_ORDER);
  private Set<Partner> _partners = new TreeSet<Partner>();

  private int _totalTransactions = 0;
  private ArrayList<Transaction> _transactions = new ArrayList<Transaction>();
  private ArrayList<Transaction> _unpaidTransactions = new ArrayList<Transaction>();
  private Map<Partner, ArrayList<Transaction>> _salesByPartner = new HashMap<Partner, ArrayList<Transaction>>();
  private Map<Partner, ArrayList<Transaction>> _acquisitionsByPartner = new HashMap<Partner, ArrayList<Transaction>>();
  private Map<Partner, ArrayList<Transaction>> _salesPaidByPartner = new HashMap<Partner, ArrayList<Transaction>>();

  private NotificationStation _notStation = new NotificationStation();

  // Getters

  /**
   * @@return warehouse's date
   */
  public int getDate() {
    return _date;
  }

  /**
   * @@return warehouse's available balance
   */
  public double getAvailableBalance() {
    return _availableBalance;
  }

  /**
   * @@return warehouse's contabilistic balance
   */
  public double getContabilisticBalance() {
    _contabilisticBalance = _availableBalance;

    for (Transaction t: _unpaidTransactions) {
      t.updateRealValue(_date);
      _contabilisticBalance += t.getRealValue();
    }

    return _contabilisticBalance;
  }

  /**
   * @@param days number of days to advance
   * @@throws NoSuchDateException
   */
  public void advanceDate(int days) throws NoSuchDateException {
    if (days > 0)
      _date += days;
    else throw new NoSuchDateException(days);
  }

  /**
   * @@param id product's id
   * @@return product
   * @@throws NoSuchProductException
   */
  public Product lookupProduct(String id) throws NoSuchProductException {
    if (!_productLookup.containsKey(id)) {
      throw new NoSuchProductException(id);
    }
    return _productLookup.get(id);
  }

  /**
   * @@param id partner's id
   * @@return partner
   * @@throws NoSuchProductException
   */
  public Partner lookupPartner(String id) throws NoSuchPartnerException {
    if (!_partnerLookup.containsKey(id)) {
      throw new NoSuchPartnerException(id);
    }
    return _partnerLookup.get(id);
  }

  /**
   * @@return sorted list of all products
   */
  public Set<Product> listAllProducts() {
    return _products;
  }

  /**
   * @@return sorted list of all batches
   */
  public Set<Batch> listAllBatches() {
    return _batches;
  }

  /**
   * @@param partner partner whose batches are to be listed
   * @@return sorted list of partner's batches
   */
  public Set<Batch> listBatchesByPartner(Partner partner) {
    Set<Batch> batchList = _batchesByPartner.get(partner);
    return batchList;
  }

  /**
   * @@param product product which batches are to be listed
   * @@return sorted list of batches
   */
  public Set<Batch> listBatchesByProduct(Product product) {
    Set<Batch> batchList = _batchesByProduct.get(product);
    return batchList;
  }

  /**
   * @@param partner partner whose notifications are to be listed
   * @@return list of all selected partner's notifications
   */
  public List<Notification> listPartnerNotifications(Partner partner) {
    return partner.listAllNotifications();
  }

  /**
   * @@return sorted list of all partners
   */
  public Set<Partner> listAllPartners() {
    return _partners;
  }

  /**
   * @@param id partners's id
   * @@param name partner's name
   * @@param address partner's address
   * @@throws DuplicatePartnerException
   */
  public void registerNewPartner(String id, String name, String address) throws DuplicatePartnerException {
    try {
      lookupPartner(id);
    } catch (NoSuchPartnerException e) {
      Partner newPartner = new Partner(id, name, address);
      _partners.add(newPartner);
      _partnerLookup.put(id, newPartner);
      _batchesByPartner.put(newPartner, new TreeSet<Batch>());
      _salesByPartner.put(newPartner, new ArrayList<Transaction>());
      _acquisitionsByPartner.put(newPartner, new ArrayList<Transaction>());
      _salesPaidByPartner.put(newPartner, new ArrayList<Transaction>());
      return;
    }

    throw new DuplicatePartnerException(id);
  }

  /**
   * @@param id   product's id
   * @@param price product's price
   * @@param stock product's stock
   * @@param return registered product
   */
  public ProductSimple registerProductSimple(String id, float price, int stock) {
    ProductSimple product;

    try {
      product = (ProductSimple) lookupProduct(id);
    } catch (NoSuchProductException e) {
      product = new ProductSimple(id);
      _productLookup.put(id, product);
      _batchesByProduct.put(product, new TreeSet<Batch>());
      _products.add(product);
    }

    product.addStock(stock);
    if (product.getMaxPrice() < price) {
      product.setMaxPrice(price);
    }

    return product;
  }

  /**
   * @@param id product's id
   * @@param recipe product's recipe
   * @@param multiplier product's multiplier
   * @@param price product's price
   * @@param stock product's stock
   * @@return registered product
   */
  public ProductDerivative registerProductDerivative(String id, Recipe recipe, float multiplier, float price, int stock) {
    ProductDerivative product;

    try {
      product = (ProductDerivative) lookupProduct(id);
    } catch (NoSuchProductException e) {
      product = new ProductDerivative(id, recipe, multiplier);
      _productLookup.put(id, product);
      _batchesByProduct.put(product, new TreeSet<Batch>());
      _products.add(product);
    }

    product.addStock(stock);
    if (product.getMaxPrice() < price) {
      product.setMaxPrice(price);
    }

    return product;
  }

  public Set<Batch> listBatchesUnderGivenPrice(float price) {
    Set<Batch> batchSet = new TreeSet<Batch>();

    for (Batch b : _batches) {
      if (b.getPrice() < price) {
        batchSet.add(b);
      }
    }

    return batchSet;
  }

  public void toggleProductNotifications(Partner partner, Product product) {
    partner.getMailbox().toggleBlockedProduct(product);
  }

  // Transactions ------------------------------------------------------------------------------------------------------

  public Batch getCheapestBatch(Product product) {
    Set<Batch> batchesByProduct = listBatchesByProduct(product);
    Batch cheapestBatch = null;
    float cheapestPrice = -1;

    for (Batch b : batchesByProduct) {
      if (cheapestPrice == -1 || b.getPrice() < cheapestPrice) {
        cheapestBatch = b;
      }
    }

    return cheapestBatch;
  }

  public void removeBatch(Batch batch) {
    _batches.remove(batch);
    _batchesByPartner.remove(batch);
    _batchesByProduct.remove(batch);
  }

  public float consumeProducts(Product product, int quantity) { // Returns the price of all of the products summed together
    float price = 0;
    Batch currentBatch;

    while (quantity > 0) {
      currentBatch = getCheapestBatch(product);

      if (currentBatch.getStock() < quantity) {
        price += currentBatch.getStock() * currentBatch.getPrice();
        quantity -= currentBatch.getStock();
        removeBatch(currentBatch);
      } else {
        price += quantity * currentBatch.getPrice();
        currentBatch.addStock(-quantity);
        quantity = 0;
      }
    }

    return price;
  }

  public Batch lookupSimilarBatch(Product product, Partner partner, float price) {
    for (Batch b : _batchesByProduct.get(product)) {
      if (b.getProduct() == product && b.getPartner() == partner && b.getPrice() == price) {
        return b;
      }
    }

    return null;
  }

  public void craftProduct(ProductDerivative product, Partner partner, int quantity) {
    Recipe recipe = product.getRecipe();

    while (quantity > 0) { // While quantity requested hasn't been reached
      float price = 0;

      for (Product p : recipe.getProducts()) { // Consume each of the recipe's products
        if (p.getStock() < recipe.getProductQuantity(p)) {
          craftProduct((ProductDerivative) p, partner, recipe.getProductQuantity(p) - p.getStock());
        }

        price += consumeProducts(p, recipe.getProductQuantity(p)); // Price gets summed
      }

      price *= (1 + product.getMultiplier()); // Calculate price of the new batch
      Batch similarBatch = lookupSimilarBatch(product, partner, price); // Look for an already existing batch

      if (similarBatch != null) { // If a similar batch exists, add stock to that one
        similarBatch.addStock(1);
      } else { // Else, create a new batch
        registerNewBatch(product, partner, price, 1);
      }

      quantity--;
    }
  }

  public Sale attemptSale(Partner partner, Product product, int amount, int deadline) throws NotEnoughProductsException {
    if (product.getStock() <= amount) { // Check if product stock is directly enough to sell
      float price = consumeProducts(product, amount);
      Sale sale = new Sale(_totalTransactions++, partner, product, amount, price, price, deadline); // FIXME
      _transactions.add(sale);
      _unpaidTransactions.add(sale);
      lookupSalesByPartner(partner).add(sale);

      return sale;
    } else { // If product stock isn't enough, check if difference between stock and requested amount can be crafted
      int stockNeeded = amount - product.getStock(); // Calculate difference

      if (product.enoughStock(amount)) { // Check if it can be crafted
        craftProduct((ProductDerivative) product, partner, stockNeeded); // Craft the product
        return attemptSale(partner, product, amount, deadline); // Try to sell again
      }
    }

    // If none of the above are successful, throw exception
    throw new NotEnoughProductsException();
  }

  public Acquisition acquire(Partner partner, Product product, int amount, float price) {
    _availableBalance -= amount * price;
    _contabilisticBalance -= amount * price;

    putProductForSale(product, partner, price, amount);

    Acquisition acquisition = new Acquisition(_totalTransactions++, partner, product, amount, price, _date);
    _transactions.add(acquisition);
    lookupAcquisitionsByPartner(partner).add(acquisition);
    _availableBalance -= acquisition.getRealValue();
    _contabilisticBalance -= acquisition.getRealValue();

    return acquisition;
  }

  public Breakdown attemptBreakdown(Partner partner, Product product, int amount) throws NotEnoughProductsException {
    if (product.getStock() < amount) { throw new NotEnoughProductsException(); } // If not enough stock, fails
    if (product.getRecipe() == null) { return null; } // If product is simple, don't do anything

    Recipe recipe = product.getRecipe();

    float price = 0;
    price += consumeProducts(product, amount);
    Receipt receipt = new Receipt(product.getRecipe(), amount);
    float productPrice;

    for (Product p: recipe.getProducts()) { // Go to each product in the recipe
      Batch batch = getCheapestBatch(p);
      if (batch == null) { // Else make one with highest price ever registered
        putProductForSale(p, partner, p.getMaxPrice(), amount * recipe.getProductQuantity(p));
        productPrice = p.getMaxPrice() * amount * recipe.getProductQuantity(p);
      } else { // If there is a batch, add to cheapest batch.
        batch.addStock(amount);
        productPrice = p.getMaxPrice() * amount * recipe.getProductQuantity(p);
      }

      receipt.productSetPrice(p, productPrice); // Set the price in the receipt
      price -= productPrice; // Subtract the product price from the final price
    }

    Breakdown breakdown = new Breakdown(_totalTransactions++, partner, product, amount, price, getDate(), receipt);
    pay(breakdown);
    _contabilisticBalance += breakdown.getRealValue();

    return breakdown;
  }

  public void pay(Transaction transaction) {
    transaction.markAsPaid();

    if (_unpaidTransactions.contains(transaction)) {
      _unpaidTransactions.remove(transaction);
      _salesPaidByPartner.get(transaction.getPartner()).add(transaction);
    }

    _availableBalance += transaction.getRealValue();
  }

  public ArrayList<Transaction> lookupPaidSalesByPartner(Partner partner) {
    for (Transaction t: _salesPaidByPartner.get(partner)) {
      t.updateRealValue(_date);
    }

    return _salesPaidByPartner.get(partner);
  }

  public ArrayList<Transaction> lookupSalesByPartner(Partner partner) {
    for (Transaction t: _salesByPartner.get(partner)) {
      t.updateRealValue(_date);
    }

    return _salesByPartner.get(partner);
  }

  public ArrayList<Transaction> lookupAcquisitionsByPartner(Partner partner) {
    return _acquisitionsByPartner.get(partner);
  }

  public Transaction lookupTransaction(int id) throws NoSuchTransactionException {
    if (id > _transactions.size()) { throw new NoSuchTransactionException(id); }

    return _transactions.get(id);
  }

  // -------------------------------------------------------------------------------------------------------------------

  /**
   * @@param product product associated with batch
   * @@param partner partner associated with batch
   * @@param price product's price
   * @@param stock product's stock
   * @@throws UnavailableFileException
   */
  public void registerNewBatch (Product product, Partner partner, float price, int stock){
    Batch batch = new Batch(product, partner, price, stock);
    product.addStock(stock);

    _batches.add(batch);
    _batchesByPartner.get(partner).add(batch);
    _batchesByProduct.get(product).add(batch);
  }

  public void putProductForSale(Product product, Partner partner, float price, int stock) {
    Batch batch = lookupSimilarBatch(product, partner, price);

    // If stock was 0, then emit a notification for NEW
    if (product.getStock() == 0) { _notStation.emitNotification(new Notification(product, price)); }

    // If new price is cheaper than old cheapest batch, then emit a notification for BARGAIN
    if (getCheapestBatch(product).getPrice() > price) { _notStation.emitNotification(new Notification(product, price)); }

    if (batch == null) { registerNewBatch(product, partner, price, stock); }
    else { batch.addStock(stock); }

  }

  /**
   * @param txtfile filename to be loaded.
   * @throws IOException
   * @throws BadEntryException
   * @throws DuplicatePartnerException
   * @throws NoSuchPartnerExceprion
   * @throws NoSuchProductException
   */
  public void importFile (String txtfile) throws
          IOException, BadEntryException, DuplicatePartnerException, NoSuchPartnerException, NoSuchProductException {
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

          ProductSimple product = registerProductSimple(id, price, stock);
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

          for (int i = 0; i < recipeStrings.length; i++) {
            String[] ss = recipeStrings[i].split(":");
            recipe.addProduct(lookupProduct(ss[0]), Integer.parseInt(ss[1]));
          }

          ProductDerivative product = registerProductDerivative(id, recipe, multiplier, price, stock);
          Partner partner = lookupPartner(partnerId);
          registerNewBatch((Product) product, partner, price, stock);
        }
        default -> throw new BadEntryException(fields[0]);
      }
    }
  }

}

