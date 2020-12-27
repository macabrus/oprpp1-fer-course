package hr.fer.oprpp1.hw08.vjezba;

import javax.swing.*;

public class FormLocalizationProvider extends LocalizationProviderBridge {

  private final JFrame frame;

  public FormLocalizationProvider(ILocalizationProvider provider, JFrame frame) {
    super(provider);
    this.frame = frame;
  }


}
