package ggc.app.main;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.WarehouseManager;
//FIXME import classes
import ggc.exceptions.MissingFileAssociationException;
import java.io.FileNotFoundException;
import java.io.IOException;
import pt.tecnico.uilib.forms.Form;

/**
 * Save current state to file under current name (if unnamed, query for name).
 */
class DoSaveFile extends Command<WarehouseManager> {

  /** @param receiver */
  DoSaveFile(WarehouseManager receiver) {
    super(Label.SAVE, receiver);

  }

  @Override
  public final void execute() throws CommandException {
    if (_receiver.missingFilename()) {
      Form form = new Form();
      _receiver.setFilename(form.requestString(Prompt.newSaveAs()));
    }

    try {
      _receiver.saveAs(_receiver.getFilename());
    } catch (MissingFileAssociationException e) {}
    catch (FileNotFoundException e) {}
    catch (IOException e) {}
  }

}
