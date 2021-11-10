package ggc.app.transactions;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.WarehouseManager;
import ggc.exceptions.*;
import ggc.app.exceptions.*;
//FIXME import classes

/**
 * Show specific transaction.
 */
public class DoShowTransaction extends Command<WarehouseManager> {

  public DoShowTransaction(WarehouseManager receiver) {
    super(Label.SHOW_TRANSACTION, receiver);
    addIntegerField("transaction", Prompt.transactionKey());
  }

  @Override
  public final void execute() throws CommandException {
    try {
      _display.popup(_receiver.requestShowTransaction(integerField("transaction")));
    } catch (NoSuchTransactionException e) {
      throw new UnknownTransactionKeyException(e.getId());
    }
  }

}
