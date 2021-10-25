package ggc;

//FIXME import classes (cannot import from pt.tecnico or ggc.app)
import java.io.*;
import java.util.*;
import ggc.exceptions.*;

/** Façade for access. */
public class WarehouseManager {

  /** Name of file storing current store. */
  private String _filename = "";

  private boolean _missingFilename = true;

  /** The warehouse itself. */
  private Warehouse _warehouse = new Warehouse();


  public boolean missingFilename() { return _missingFilename; }

  public String getFilename() { return _filename; }

  public void requestDateToAdvance(int days) throws NoSuchDateException {
    _warehouse.advanceDate(days);
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

  public List<Product> requestListAllProducts() {
    return _warehouse.listAllProducts();
  }

  public List<Batch> requestListAllBatches() {
    return _warehouse.listAllBatches();
  }

  public List<Batch> requestListBatchesByProduct(String id) throws NoSuchProductException {
    return _warehouse.listBatchesByProduct(_warehouse.lookupProduct(id));
  }

  public List<Batch> requestListBatchesByPartner(String id) throws NoSuchPartnerException {
    return _warehouse.listBatchesByPartner(_warehouse.lookupPartner(id));
  }

  public void requestRegisterPartner(String id, String name, String address) throws DuplicatePartnerException {
    _warehouse.registerNewPartner(id, name, address);
  }

  public List<Partner> requestListAllPartners () {
    return _warehouse.listAllPartners();
  }

  public Partner requestShowPartner(String id) throws NoSuchPartnerException {
    return _warehouse.lookupPartner(id);
  }

  public List<Notification> requestListPartnerNotifications(String id) throws NoSuchPartnerException {
    return _warehouse.listPartnerNotifications(_warehouse.lookupPartner(id));
  }

  /**
   * @@throws IOException
   * @@throws FileNotFoundException
   * @@throws MissingFileAssociationException
   */
  public void save() throws IOException, FileNotFoundException, MissingFileAssociationException {
    if (!_filename.equals("")) _missingFilename = false;
    if (missingFilename()) {throw new MissingFileAssociationException();}
    ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(_filename)));
    oos.writeObject(_warehouse);
    oos.close();
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
    } catch (IOException | BadEntryException | DuplicatePartnerException | NoSuchPartnerException | NoSuchProductException | BadEntryException e) {
	      throw new ImportFileException(textfile);
    }
  }
}
