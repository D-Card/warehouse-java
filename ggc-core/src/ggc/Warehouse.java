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

  /** Serial number for serialization. */
  private static final long serialVersionUID = 202109192006L;

  /** Warehouse's current available balance */
  private double _availableBalance = 0;
  /** Warehouse's current contabilistic balance */
  private double _contabilisticBalance = 0;
  /** Warehouse's current date */
  private int _date = 0;
  /** Set of all the products the warehouse knows */
  private Set<Product> _products = new TreeSet<Product>();
  /** Map matching products with their id */
  private Map<String, Product> _productLookup = new TreeMap<String, Product>(String.CASE_INSENSITIVE_ORDER);
  private PriorityQueue<Batch> _batches = new PriorityQueue<Batch>();
  private Map<String, Partner> _partnerLookup = new TreeMap<String, Partner>(String.CASE_INSENSITIVE_ORDER);
  private Set<Partner> _partners = new TreeSet<Partner>();

  private ArrayList<Transaction> _transactions = new ArrayList<Transaction>();

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

    for (Transaction t: _transactions) {
      if (!t.paid()) {
        t.updateRealValue(_date);
        _contabilisticBalance += t.getRealValue();
      }
    }

    return _contabilisticBalance;
  }

  public int getTotalTransactions() {
    return _transactions.size();
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

  public ArrayList<String> lookupSpecificPartner(String id) throws NoSuchPartnerException {
    ArrayList<String> stringList = new ArrayList<String>();
    Partner partner = lookupPartner(id);

    stringList.add(partner.toString());
    for (Notification n: listPartnerNotificationsByMethod(partner, "")) {
      stringList.add(n.toString());
    }

    return stringList;
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
  public PriorityQueue<Batch> listAllBatches() {
    return _batches;
  }

  /**
   * @@param id id of the partner whose batches are to be listed
   * @@return sorted list of partner's batches
   */
  public PriorityQueue<Batch> listBatchesByPartner(String partner) throws NoSuchPartnerException {
    return listBatchesByPartner(lookupPartner(partner));
  }

  /**
   * @@param partner partner whose batches are to be listed
   * @@return sorted list of partner's batches
   */
  public PriorityQueue<Batch> listBatchesByPartner(Partner partner) {
    return partner.getBatches();
  }

  /**
   * @@param id id of the product which batches are to be listed
   * @@return sorted list of batches
   */
  public PriorityQueue<Batch> listBatchesByProduct(String partner) throws NoSuchProductException{
    return listBatchesByProduct(lookupProduct(partner));
  }

  /**
   * @@param product product which batches are to be listed
   * @@return sorted list of batches
   */
  public PriorityQueue<Batch> listBatchesByProduct(Product product) {
    return product.getBatches();
  }


    /**
   * @@param id id of the partner whose notifications are to be listed
   * @@return list of all selected partner's notifications
   */
  public List<Notification> listPartnerNotifications(String id) throws NoSuchPartnerException{
    return lookupPartner(id).listAllNotifications();
  }


  public List<Notification> listPartnerNotificationsByMethod(String id, String method) throws NoSuchPartnerException{
    return listPartnerNotificationsByMethod(lookupPartner(id), method);
  }

  public List<Notification> listPartnerNotificationsByMethod(Partner partner, String method) throws NoSuchPartnerException{
    return partner.listAllNotificationsByMethod(method);
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
      _products.add(product);
    }

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
      _products.add(product);
    }

    if (product.getMaxPrice() < price) {
      product.setMaxPrice(price);
    }

    return product;
  }

  public PriorityQueue<Batch> listBatchesUnderGivenPrice(float price) {
    PriorityQueue<Batch> batchQueue = new PriorityQueue<Batch>();

    for (Batch b : listAllBatches()) {
      if (b.getPrice() < price) {
        batchQueue.add(b);
      }
    }

    return batchQueue;
  }

  public void toggleProductNotifications(String partnerStr, String productStr) throws NoSuchPartnerException, NoSuchProductException {
    Partner partner = lookupPartner(partnerStr);
    Product product = lookupProduct(productStr);
    partner.getMailbox().toggleBlockedProduct(product);
  }

  // Transactions ------------------------------------------------------------------------------------------------------

  public Batch getCheapestBatch(Product product) {
    PriorityQueue<Batch> batchesByProduct = listBatchesByProduct(product);
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
    batch.getPartner().removeBatch(batch);
    batch.getProduct().removeBatch(batch);
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
    for (Batch b : listBatchesByProduct(product)) {
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

      registerNewBatch(product, partner, price, 1);
      quantity--;
    }
  }

  public Sale attemptSale(String partnerStr, String productStr, int amount, int deadline) throws NotEnoughProductsException, NoSuchPartnerException, NoSuchProductException {

    Partner partner = lookupPartner(partnerStr);
    Product product = lookupProduct(productStr);
    return attemptSale(partner, product, amount, deadline);
  }
   

  public Sale attemptSale(Partner partner, Product product, int amount, int deadline) throws NotEnoughProductsException {
    if (product.getStock() <= amount) { // Check if product stock is directly enough to sell
      float price = consumeProducts(product, amount);
      Sale sale = new Sale(getTotalTransactions(), partner, product, amount, price, price, deadline); // FIXME
      _transactions.add(sale);
      partner.addSale(sale);
      product.addStock(-amount);

      return sale;
    } else { // If product stock isn't enough, check if difference between stock and requested amount can be crafted
      int stockNeeded = amount - product.getStock(); // Calculate difference

      if (product.enoughStock(amount)) { // Check if it can be crafted
        craftProduct((ProductDerivative) product, partner, stockNeeded); // Craft the product
        return attemptSale(partner, product, amount, deadline); // Try to sell again
      }
    }

    // If none of the above are successful, throw exception
    throw new NotEnoughProductsException(product.getStock());
  }

  public Acquisition acquire(String partnerStr, String productStr, int amount, float price) throws NoSuchPartnerException, NoSuchProductException{
    Partner partner = lookupPartner(partnerStr);
    Product product = lookupProduct(productStr);
    return acquire(partner, product, amount, price);
  }

  public Acquisition acquire(Partner partner, Product product, int amount, float price) {
    _availableBalance -= amount * price;
    _contabilisticBalance -= amount * price;

    registerNewBatch(product, partner, price, amount);

    Acquisition acquisition = new Acquisition(getTotalTransactions(), partner, product, amount, price, _date);

    _transactions.add(acquisition);
    lookupAcquisitionsByPartner(partner).add(acquisition);

    _availableBalance -= acquisition.getRealValue();
    _contabilisticBalance -= acquisition.getRealValue();

    return acquisition;
  }


  public Breakdown attemptBreakdown(String partnerStr, String productStr, int amount) throws NotEnoughProductsException, NoSuchPartnerException, NoSuchProductException {
    Partner partner = lookupPartner(partnerStr);
    Product product = lookupProduct(productStr);
    return attemptBreakdown(partner, product, amount);
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

      if (p.getStock() == 0) { // If there is no batch, create one with highest price ever registered
        productPrice = p.getMaxPrice();
      } else { // If there is a batch, create one with the same price.
        productPrice = getCheapestBatch(p).getPrice();
      }

      registerNewBatch(p, partner, productPrice, amount * recipe.getProductQuantity(p));
      receipt.productSetPrice(p, productPrice); // Set the price in the receipt
      price -= productPrice * amount * recipe.getProductQuantity(p); // Subtract the product price from the final price
    }

    Breakdown breakdown = new Breakdown(getTotalTransactions(), partner, product, amount, price, getDate(), receipt);
    pay(breakdown);
    _contabilisticBalance += breakdown.getRealValue();

    return breakdown;
  }

  public void pay(int id) throws NoSuchTransactionException{
    Transaction transaction = lookupTransaction(id);
    pay(transaction);
  }

  public void pay(Transaction transaction) {
    transaction.markAsPaid();

    _availableBalance += transaction.getRealValue();
  }

  public ArrayList<Transaction> lookupPaidSalesByPartner(String partner) throws NoSuchPartnerException {
    return lookupPaidSalesByPartner(lookupPartner(partner));
  }

  public ArrayList<Transaction> lookupPaidSalesByPartner(Partner partner) {
    ArrayList<Transaction> paidSales = new ArrayList<Transaction>();

    for (Transaction t: paidSales) {
      t.updateRealValue(_date);
    }

    return paidSales;
  }

  public ArrayList<Transaction> lookupSalesByPartner(String partner) throws NoSuchPartnerException{
    return lookupSalesByPartner(lookupPartner(partner));
  }

  public ArrayList<Transaction> lookupSalesByPartner(Partner partner) {
    for (Transaction t: partner.getSales()) {
      t.updateRealValue(_date);
    }

    return partner.getSales();
  }

      
  public void acquireNewProductSimple(String partner, String product, float price, int stock) throws NoSuchPartnerException, NoSuchProductException{
    registerProductSimple(product, price, stock);
    acquire(partner, product, stock, price);
  }


  public void acquireNewProductDerivative(String partnerStr, String productStr, float price, int stock, ArrayList<String> products, ArrayList<Integer> productQuantities, float multiplier) throws NoSuchPartnerException, NoSuchProductException {
      Partner partner = lookupPartner(partnerStr);
      Recipe recipe = new Recipe();

      for (int i = 0; i < products.size(); i++) {
        recipe.addProduct(lookupProduct(products.get(i)), productQuantities.get(i));
      }

      registerProductDerivative(productStr, recipe, multiplier, price, stock);
      Product product = lookupProduct(productStr);

      acquire(partner, product, stock, price);
  }  

  public ArrayList<Transaction> lookupAcquisitionsByPartner(String partner) throws NoSuchPartnerException{
    return lookupAcquisitionsByPartner(lookupPartner(partner));
  }


  public ArrayList<Transaction> lookupAcquisitionsByPartner(Partner partner) {
    return partner.getAcquisitions();

  }

  public Transaction lookupTransaction(int id) throws NoSuchTransactionException {
    if (id >= _transactions.size() || id < 0) { throw new NoSuchTransactionException(id); }

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
    Batch cheapestBatch = getCheapestBatch(product);

    // If stock was 0, then emit a notification for NEW
    if (product.getStock() == 0) { _notStation.emitNotification(new Notification("NEW", product, price)); }

    // If new price is cheaper than old cheapest batch, then emit a notification for BARGAIN
    if (cheapestBatch != null && cheapestBatch.getPrice() > price) { _notStation.emitNotification(new Notification("BARGAIN", product, price)); }

    product.addStock(stock);

    _batches.add(batch);

    partner.addBatch(batch);
    product.addBatch(batch);
  }

  /**
   * @param txtfile filename to be loaded.
   * @throws IOException
   * @throws BadEntryException
   * @throws DuplicatePartnerException
   * @throws NoSuchPartnerException
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

