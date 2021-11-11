package ggc.app.partners;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.WarehouseManager;
import ggc.exceptions.NoSuchPartnerException;
import ggc.app.exceptions.UnknownPartnerKeyException;
//FIXME import classes

/**
 * Show all transactions for a specific partner.
 */
class DoShowPartnerAcquisitions extends Command<WarehouseManager> {

  DoShowPartnerAcquisitions(WarehouseManager receiver) {
    super(Label.SHOW_PARTNER_ACQUISITIONS, receiver);
    addStringField("partner", Prompt.partnerKey());
  }

  @Override
  public void execute() throws CommandException {
    try {
      _display.popup(_receiver.requestShowPartnerAcquisitions(stringField("partner")));
    } catch (NoSuchPartnerException e) {
      throw new UnknownPartnerKeyException(stringField("partner"));
    }
  }

}
