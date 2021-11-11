package ggc.app.transactions;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.WarehouseManager;
import ggc.exceptions.*;
import ggc.app.exceptions.*;
import pt.tecnico.uilib.forms.Form;
import java.util.*;
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
      if (!Form.confirm(Prompt.addRecipe())) { // If it's a simple product
        try {_receiver.requestAcquireNewProductSimple(
                stringField("partner"),
                stringField("product"),
                realField("price").floatValue(),
                integerField("amount"));
        } catch (NoSuchPartnerException | NoSuchProductException e2) {} //this never happens
      } else { // If it's a derivative product
        int productsLeft = Form.requestInteger(Prompt.numberOfComponents());
        float multiplier = Form.requestReal(Prompt.alpha()).floatValue();
        ArrayList<String> productStrings = new ArrayList<String>();
        ArrayList<Integer> productQuantities = new ArrayList<Integer>();

        while (productsLeft > 0) {
          productStrings.add(Form.requestString(Prompt.productKey()));
          productQuantities.add(Form.requestInteger(Prompt.amount()));

          productsLeft --;
        }
        try{
        _receiver.requestAcquireNewProductDerivative(
                stringField("partner"),
                stringField("product"),
                realField("price").floatValue(),
                integerField("amount"),
                productStrings,
                productQuantities,
                multiplier);
      } catch (NoSuchPartnerException | NoSuchProductException e3) {} //this never happens
      }

    } catch (NotEnoughProductsException e) {}
  }

}
