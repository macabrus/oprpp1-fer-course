package hr.fer.oprpp1.hw08.vjezba;

import java.util.Locale;
import java.util.ResourceBundle;

public class LocalizationProvider extends AbstractLocalizationProvider {

  private static final LocalizationProvider instance = new LocalizationProvider();
  private String language;
  private ResourceBundle bundle;

  private LocalizationProvider() {
    this.bundle = ResourceBundle.getBundle("prijevodi", Locale.ENGLISH);
    this.language = "en";
  }

  public static LocalizationProvider getInstance() {
    return instance;
  }

  @Override
  public String getString(String key) {
    return bundle.getString(key);
  }

  public void setLanguage(String language) {
    bundle = ResourceBundle.getBundle("prijevodi", Locale.forLanguageTag(language));
    this.language = language;
    fire(); // Notify listeners on change
  }

}
