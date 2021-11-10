package ggc;

import java.io.*;
import java.util.*;
import ggc.exceptions.*;
import ggc.partners.*;
import ggc.products.*;
import ggc.transactions.*;

/** Façade for access. */
public class WarehouseManager {

  /** Name of file storing current store. */
  private String _filename = "";

  private boolean _missingFilename = true;
  private boolean _updated = true;

  /** The warehouse itself. */
  private Warehouse _warehouse = new Warehouse();


  public boolean missingFilename() { return _missingFilename; }

  public String getFilename() { return _filename; }

  public void requestDateToAdvance(int days) throws NoSuchDateException {
    _warehouse.advanceDate(days);
    _updated = true;
  }

  public double requestAvailableBalance() {
    return _warehouse.getAvailableBalance();
  }

  public double requestContabilisticBalance() {
    return _warehouse.getContabilisticBalance();
  }

  public int requestDate(){
    return _warehouse.getDate();
  }

  public Set<Product> requestListAllProducts() {
    return _warehouse.listAllProducts();
  }

  public PriorityQueue<Batch> requestListAllBatches() {
    return _warehouse.listAllBatches();
  }

  public PriorityQueue<Batch> requestListBatchesByProduct(String id) throws NoSuchProductException {
    return _warehouse.listBatchesByProduct(_warehouse.lookupProduct(id));
  }

  public PriorityQueue<Batch> requestListBatchesByPartner(String id) throws NoSuchPartnerException {
    return _warehouse.listBatchesByPartner(_warehouse.lookupPartner(id));
  }

  public void requestRegisterPartner(String id, String name, String address) throws DuplicatePartnerException {
    _warehouse.registerNewPartner(id, name, address);
    _updated = true;
  }

  public Set<Partner> requestListAllPartners () {
    return _warehouse.listAllPartners();
  }

  public Partner requestShowPartner(String id) throws NoSuchPartnerException {
    return _warehouse.lookupPartner(id);
  }

  public List<Notification> requestListPartnerNotifications(String id) throws NoSuchPartnerException {
    return _warehouse.listPartnerNotifications(_warehouse.lookupPartner(id));
  }

  public List<Notification> requestListPartnerNotificationsByMethod(String id, String method) throws NoSuchPartnerException {
    return _warehouse.listPartnerNotificationsByMethod(_warehouse.lookupPartner(id), method);
  }

  public PriorityQueue<Batch> requestListBatchesUnderGivenPrice(float price) {
    return _warehouse.listBatchesUnderGivenPrice(price);
  }

  public void requestToggleProductNotifications(String partnerStr, String productStr) throws NoSuchPartnerException, NoSuchProductException {
    Partner partner = _warehouse.lookupPartner(partnerStr);
    Product product = _warehouse.lookupProduct(productStr);

    _warehouse.toggleProductNotifications(partner, product);
  }

  public ArrayList<Transaction> requestShowPartnerPaidSales(String id) throws NoSuchPartnerException {
    Partner partner = _warehouse.lookupPartner(id);

    return _warehouse.lookupPaidSalesByPartner(partner);
  }

  public ArrayList<Transaction> requestShowPartnerSales(String id) throws NoSuchPartnerException {
    Partner partner = _warehouse.lookupPartner(id);

    return _warehouse.lookupSalesByPartner(partner);
  }

  public ArrayList<Transaction> requestShowPartnerAcquisitions(String id) throws NoSuchPartnerException {
    Partner partner = _warehouse.lookupPartner(id);

    return _warehouse.lookupAcquisitionsByPartner(partner);
  }

  public Transaction requestShowTransaction(int id) throws NoSuchTransactionException {
    return _warehouse.lookupTransaction(id);
  }

  public void requestPay(int id) throws NoSuchTransactionException {
    _warehouse.pay(_warehouse.lookupTransaction(id));
  }

    public void requestAttemptBreakdown(String partnerString, String productString, int amount) throws NotEnoughProductsException, NoSuchPartnerException, NoSuchProductException {
    Partner partner = _warehouse.lookupPartner(partnerString);
    Product product = _warehouse.lookupProduct(productString);

    _warehouse.attemptBreakdown(partner, product, amount);
  }

  public void requestAttemptSale(String partnerString, int deadline, String productString, int amount) throws NotEnoughProductsException, NoSuchPartnerException, NoSuchProductException {
    Partner partner = _warehouse.lookupPartner(partnerString);
    Product product = _warehouse.lookupProduct(productString);

    _warehouse.attemptSale(partner, product, amount, deadline);
  }

  public void requestAcquire(String partnerString, String productString, float price, int amount) throws NotEnoughProductsException, NoSuchPartnerException, NoSuchProductException {
    Partner partner = _warehouse.lookupPartner(partnerString);
    Product product = _warehouse.lookupProduct(productString);

    _warehouse.acquire(partner, product, amount, price);
  }

  public void requestRegisterProductSimple(String partnerString, String productString, float price, int stock){
    try {
      Partner partner = _warehouse.lookupPartner(partnerString);
      _warehouse.registerProductSimple(productString, price, stock);
      Product product = _warehouse.lookupProduct(productString);

      _warehouse.acquire(partner, product, stock, price);
    } catch (NoSuchPartnerException | NoSuchProductException e) {}
  }

  public void requestRegisterProductDerivative(String partnerString, String productString, float price, int stock, ArrayList<String> products, ArrayList<Integer> productQuantities, float multiplier) {
    try {
      Partner partner = _warehouse.lookupPartner(partnerString);
      Recipe recipe = new Recipe();

      for (int i = 0; i < products.size(); i++) {
        recipe.addProduct(_warehouse.lookupProduct(products.get(i)), productQuantities.get(i));
      }

      _warehouse.registerProductDerivative(productString, recipe, multiplier, price, stock);
      Product product = _warehouse.lookupProduct(productString);

      _warehouse.acquire(partner, product, stock, price);
    } catch (NoSuchPartnerException | NoSuchProductException e) {}
  }

  /**
   * @@throws IOException
   * @@throws FileNotFoundException
   * @@throws MissingFileAssociationException
   */
  public void save() throws IOException, FileNotFoundException, MissingFileAssociationException {
    if (!_filename.equals("")) _missingFilename = false;
    if (missingFilename()) {throw new MissingFileAssociationException();}
    if (_updated == true) {
      ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(_filename)));
      oos.writeObject(_warehouse);
      oos.close();
      _updated = false;
    }  
  }

  /**
   * @@param filename
   * @@throws MissingFileAssociationException
   * @@throws IOException
   * @@throws FileNotFoundException
   */
  public void saveAs(String filename) throws MissingFileAssociationException, FileNotFoundException, IOException {
    _filename = filename;
    save();
  }

  /**
   * @@param filename
   * @@throws UnavailableFileException
   */
  public void load(String filename) throws UnavailableFileException {
    try {
      ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(filename)));
      _warehouse = (Warehouse) ois.readObject();
      ois.close();
      _filename = filename;
      _missingFilename = false;
      _updated = false;
    } catch (FileNotFoundException fnf) {throw new UnavailableFileException(filename);} 
    catch (IOException | ClassNotFoundException e) { e.printStackTrace(); }
    
  }

  /**
   * @param textfile
   * @throws ImportFileException
   */
  public void importFile(String textfile) throws ImportFileException {
    try {
	    _warehouse.importFile(textfile);
      _updated = true;
    } catch (IOException | BadEntryException | DuplicatePartnerException | NoSuchPartnerException | NoSuchProductException e) {
	      throw new ImportFileException(textfile);
    }
  }
}
