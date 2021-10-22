package ggc;

//FIXME import classes (cannot import from pt.tecnico or ggc.app)
import java.io.*;
import java.util.*;
import ggc.exceptions.*;

/** Façade for access. */
public class WarehouseManager {

  /** Name of file storing current store. */
  private String _filename = "";

  /** The warehouse itself. */
  private Warehouse _warehouse = new Warehouse();

  //FIXME define other attributes
  //FIXME define constructor(s)
  //FIXME define other methods

  public void requestDaysToAdvance(int days) throws NoSuchDateException {
    _warehouse.advanceDays(days);
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

  public LinkedList<Product> requestListAllProducts() {
    return _warehouse.listAllProducts();
  }

  public LinkedList<Batch> requestListAllBatches() {
    return _warehouse.listAllBatches();
  }

  public LinkedList<Batch> requestListBatchesByProduct(String id) throws NoSuchProductException {
    return _warehouse.listBatchesByProduct(_warehouse.lookupProduct(id));
  }

  public LinkedList<Batch> requestListBatchesByPartner(String id) throws NoSuchPartnerException {
    return _warehouse.listBatchesByPartner(_warehouse.lookupPartner(id));
  }

  public void requestRegisterPartner(String id, String name, String address) throws DuplicatePartnerException {
    _warehouse.registerNewPartner(id, name, address);
  }

  public LinkedList<Partner> requestListAllPartners () {
    return _warehouse.listAllPartners();
  }

  /**
   * @@throws IOException
   * @@throws FileNotFoundException
   * @@throws MissingFileAssociationException
   */
  public void save() throws IOException, FileNotFoundException, MissingFileAssociationException {
    //FIXME implement serialization method
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
    //FIXME implement serialization method
  }

  /**
   * @param textfile
   * @throws ImportFileException
   */
  public void importFile(String textfile) throws ImportFileException {
    try {
	    _warehouse.importFile(textfile);
    } catch (IOException | BadEntryException /* FIXME maybe other exceptions */ e) {
	    throw new ImportFileException(textfile);
    }
  }

}
