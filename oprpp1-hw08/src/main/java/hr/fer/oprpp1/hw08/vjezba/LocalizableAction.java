package hr.fer.oprpp1.hw08.vjezba;

import javax.swing.*;

public abstract class LocalizableAction extends AbstractAction {

  public LocalizableAction(String key, ILocalizationProvider lp) {
    putValue(NAME, lp.getString(key));
    // putValue(SHORT_DESCRIPTION, lp.getString(key + "_description"));
    lp.addLocalizationListener(() -> putValue(NAME, lp.getString(key)));
  }

}
