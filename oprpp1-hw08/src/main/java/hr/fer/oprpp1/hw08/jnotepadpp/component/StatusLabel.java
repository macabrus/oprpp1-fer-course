package hr.fer.oprpp1.hw08.jnotepadpp.component;

import hr.fer.oprpp1.hw08.vjezba.ILocalizationProvider;

import javax.swing.*;

public class StatusLabel extends JLabel {

  private final String key;
  private String val;
  private final ILocalizationProvider lp;

  public StatusLabel(String key, ILocalizationProvider lp) {
    this.key = key;
    this.lp = lp;
    lp.addLocalizationListener(this::update);
  }

  public void setValue(String value) {
    val = value;
    update();
  }

  private void update() {
    setText(lp.getString(key) + ": " + this.val);
  }
}
