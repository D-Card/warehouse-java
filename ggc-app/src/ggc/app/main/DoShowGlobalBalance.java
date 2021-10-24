package ggc.app.main;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.WarehouseManager;

/**
 * Show global balance.
 */
class DoShowGlobalBalance extends Command<WarehouseManager> {

  DoShowGlobalBalance(WarehouseManager receiver) {
    super(Label.SHOW_BALANCE, receiver);
  }

  @Override
  public final void execute() throws CommandException {
    //FIXME implement command
      double availableBalance = _receiver.requestAvailableBalance();
      double contabilisticBalance = _receiver.requestContabilisticBalance();
      _display.popup(Message.currentBalance(availableBalance, contabilisticBalance));
  }
  
}
