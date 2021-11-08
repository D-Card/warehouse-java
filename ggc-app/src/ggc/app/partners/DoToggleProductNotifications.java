package ggc.app.partners;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.WarehouseManager;
import ggc.exceptions.NoSuchProductException;
import ggc.exceptions.NoSuchPartnerException;
import ggc.app.exceptions.UnknownProductKeyException;
//FIXME import classes

/**
 * Toggle product-related notifications.
 */
class DoToggleProductNotifications extends Command<WarehouseManager> {

  DoToggleProductNotifications(WarehouseManager receiver) {
    super(Label.TOGGLE_PRODUCT_NOTIFICATIONS, receiver);
    addStringField("partner", Prompt.partnerKey());
    addStringField("product", Prompt.productKey());
  }

  @Override
  public void execute() throws CommandException, UnknownProductKeyException {
    try {
      _receiver.requestToggleProductNotifications(stringField("partner"), stringField("product"));
    } catch (NoSuchProductException e) {
      throw new UnknownProductKeyException(stringField("product"));
    } catch (NoSuchPartnerException e) {};
  }

}
