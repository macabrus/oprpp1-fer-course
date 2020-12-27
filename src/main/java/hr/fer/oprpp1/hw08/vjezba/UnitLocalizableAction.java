package hr.fer.oprpp1.hw08.vjezba;

import java.awt.event.ActionEvent;

/**
 * Implementation of localizable action that simply does nothing
 * except subscribes component to LocalizationProvider.
 * I used it during testing only
 */
public class UnitLocalizableAction extends LocalizableAction {

  public UnitLocalizableAction(String key, ILocalizationProvider lp) {
    super(key, lp);
  }

  @Override
  public void actionPerformed(ActionEvent e) {

  }
}
