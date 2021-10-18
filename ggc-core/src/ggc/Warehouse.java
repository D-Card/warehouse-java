package ggc;

// FIXME import classes (cannot import from pt.tecnico or ggc.app)
import java.io.*;
import ggc.exceptions.*;

/**
 * Class Warehouse implements a warehouse.
 */
public class Warehouse implements Serializable {

  /** Serial number for serialization. */
  private static final long serialVersionUID = 202109192006L;

  // FIXME define attributes
  // FIXME define contructor(s)
  // FIXME define methods

  public void advanceDays(int days) throws InvalidDateException {
    if (days > 0) 
      _date += days;
    else throw new InvalidDateException(days);
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