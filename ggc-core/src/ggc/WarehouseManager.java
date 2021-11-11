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

  public ArrayList<Batch> requestListAllBatches() {
    return _warehouse.listAllBatches();
  }

  public Queue<Batch> requestListBatchesByProduct(String product) throws NoSuchProductException {
    return _warehouse.listBatchesByProduct(product);
  }

  public Queue<Batch> requestListBatchesByPartner(String partner) throws NoSuchPartnerException {
    return _warehouse.listBatchesByPartner(partner);
  }

  public void requestRegisterPartner(String id, String name, String address) throws DuplicatePartnerException {
    _warehouse.registerNewPartner(id, name, address);
    _updated = true;
  }

  public Set<Partner> requestListAllPartners () {
    return _warehouse.listAllPartners();
  }

  public Partner requestShowPartner(String partner) throws NoSuchPartnerException {
    return _warehouse.lookupPartner(partner);
  }

  public List<Notification> requestListPartnerNotifications(String partner) throws NoSuchPartnerException {
    return _warehouse.listPartnerNotifications(partner);
  }

  public ArrayList<String> requestShowSpecificPartner(String id) throws NoSuchPartnerException {
    return _warehouse.lookupSpecificPartner(id);
  }

  public List<Notification> requestListPartnerNotificationsByMethod(String partner, String method) throws NoSuchPartnerException {
    return _warehouse.listPartnerNotificationsByMethod(partner, method);
  }

  public Queue<Batch> requestListBatchesUnderGivenPrice(float price) {
    return _warehouse.listBatchesUnderGivenPrice(price);
  }

  public void requestToggleProductNotifications(String partner, String product) throws NoSuchPartnerException, NoSuchProductException {
   _warehouse.toggleProductNotifications(partner, product);
   _updated = true;
  }

  public ArrayList<Transaction> requestShowPartnerPaidSales(String partner) throws NoSuchPartnerException {
    return _warehouse.lookupPaidSalesByPartner(partner);
  }

  public ArrayList<Transaction> requestShowPartnerSales(String partner) throws NoSuchPartnerException {
    return _warehouse.lookupSalesByPartner(partner);
  }

  public ArrayList<Transaction> requestShowPartnerAcquisitions(String partner) throws NoSuchPartnerException {
    return _warehouse.lookupAcquisitionsByPartner(partner);
  }

  public Transaction requestShowTransaction(int id) throws NoSuchTransactionException {
    return _warehouse.lookupTransaction(id);
  }

  public void requestPay(int id) throws NoSuchTransactionException {
    _warehouse.pay(id);
    _updated = true;
  }

    public void requestAttemptBreakdown(String partner, String product, int amount) throws NotEnoughProductsException, NoSuchPartnerException, NoSuchProductException {
    _warehouse.attemptBreakdown(partner, product, amount);
    _updated = true;
  }

  public void requestAttemptSale(String partner, int deadline, String product, int amount) throws NotEnoughProductsException, NoSuchPartnerException, NoSuchProductException {
    _warehouse.attemptSale(partner, product, amount, deadline);
    _updated = true;
  }

  public void requestAcquire(String partner, String product, float price, int amount) throws NotEnoughProductsException, NoSuchPartnerException, NoSuchProductException {
    _warehouse.acquire(partner, product, amount, price);
    _updated = true;
  }

  public void requestAcquireNewProductSimple(String partner, String product, float price, int stock) throws NoSuchPartnerException, NoSuchProductException{
    //runs when acquiring a never seen simple product
    _warehouse.acquireNewProductSimple(partner, product, price, stock);
    _updated = true;
  }

  public void requestAcquireNewProductDerivative(String partner, String product, float price, int stock, ArrayList<String> products, ArrayList<Integer> productQuantities, float multiplier) throws NoSuchPartnerException, NoSuchProductException{
    _warehouse.acquireNewProductDerivative(partner, product, price, stock, products, productQuantities, multiplier);
    _updated = true;
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
