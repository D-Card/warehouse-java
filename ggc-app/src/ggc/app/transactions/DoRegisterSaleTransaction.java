package ggc.app.transactions;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.WarehouseManager;
import ggc.exceptions.*;
import ggc.app.exceptions.*;

/**
 * 
 */
public class DoRegisterSaleTransaction extends Command<WarehouseManager> {

  public DoRegisterSaleTransaction(WarehouseManager receiver) {
    super(Label.REGISTER_SALE_TRANSACTION, receiver);
    addStringField("partner", Prompt.partnerKey());
    addIntegerField("date", Prompt.paymentDeadline());
    addStringField("product", Prompt.productKey());
    addIntegerField("amount", Prompt.amount());
    }

  @Override
  public final void execute() throws CommandException {
    try {
      _receiver.requestAttemptSale(
        stringField("partner"),
        integerField("date"),
        stringField("product"),
        integerField("amount")
      );
    } catch (NoSuchPartnerException e) {
      throw new UnknownPartnerKeyException(stringField("partner"));
    } catch (NoSuchProductException e) {
      throw new UnknownProductKeyException(stringField("product"));
    } catch (NotEnoughProductsException e) {
      throw new UnavailableProductException(e.getProduct(), e.getStockNeeded(), e.getCurrentStock());
    }
  }

}
