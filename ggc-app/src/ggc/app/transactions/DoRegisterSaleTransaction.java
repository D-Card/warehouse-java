package ggc.app.transactions;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.WarehouseManager;
//FIXME import classes

/**
 * 
 */
public class DoRegisterSaleTransaction extends Command<WarehouseManager> {

  public DoRegisterSaleTransaction(WarehouseManager receiver) {
    super(Label.REGISTER_SALE_TRANSACTION, receiver);
    addStringField("partner-id", Prompt.partnerKey());  
    addIntegerField("date", Prompt.paymentDeadline());
    addStringField("product-id", Prompt.productKey());
    addIntegerField("amount", Prompt.amount());
    }

  @Override
  public final void execute() throws CommandException {
    // _receiver.registerSaleTransaction(
    //   getStringField("partner-id"),
    //   getIntegerField("date"),
    //   getStringField("product-id"),
    //   getIntegerField("amount")
    // );
  }

}
