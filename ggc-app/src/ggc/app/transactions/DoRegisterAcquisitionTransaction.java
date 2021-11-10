package ggc.app.transactions;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.WarehouseManager;
import ggc.exceptions.*;
import ggc.app.exceptions.*;
import pt.tecnico.uilib.forms.Form;
//FIXME import classes

/**
 * Register order.
 */
public class DoRegisterAcquisitionTransaction extends Command<WarehouseManager> {

  public DoRegisterAcquisitionTransaction(WarehouseManager receiver) {
    super(Label.REGISTER_ACQUISITION_TRANSACTION, receiver);
    addStringField("partner", Prompt.partnerKey());
    addStringField("product", Prompt.productKey());
    addRealField("price", Prompt.price());
    addIntegerField("amount", Prompt.amount());
  }

  @Override
  public final void execute() throws CommandException {
    try {
      _receiver.requestAcquire(
              stringField("partner"),
              stringField("product"),
              realField("price").floatValue(),
              integerField("amount")
      );
    } catch (NoSuchPartnerException e) {
      throw new UnknownPartnerKeyException(stringField("partner"));
    } catch (NoSuchProductException e) {
      if (!Form.confirm(Prompt.addRecipe())) {
        _receiver.requestRegisterProductSimple(stringField("partner"), stringField("product"), realField("price").floatValue(), integerField("amount"));
      } else {

      }

    } catch (NotEnoughProductsException e) {}
  }

}
