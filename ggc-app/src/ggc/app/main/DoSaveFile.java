package ggc.app.main;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.WarehouseManager;
//FIXME import classes
import ggc.exceptions.MissingFileAssociationException;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Save current state to file under current name (if unnamed, query for name).
 */
class DoSaveFile extends Command<WarehouseManager> {

  /** @param receiver */
  DoSaveFile(WarehouseManager receiver) {
    super(Label.SAVE, receiver);

    addStringField("filename", Prompt.newSaveAs());

  }

  @Override
  public final void execute() throws CommandException {

    _receiver.setFilename(stringField("filename"));

    try {
      _receiver.saveAs(_receiver.getFilename());
    } catch (MissingFileAssociationException e) {}
    catch (FileNotFoundException e) {}
    catch (IOException e) {};
  }

}
