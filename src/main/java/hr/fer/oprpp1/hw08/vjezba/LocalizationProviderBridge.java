package hr.fer.oprpp1.hw08.vjezba;

public class LocalizationProviderBridge extends AbstractLocalizationProvider {

  private final ILocalizationProvider provider;
  private final ILocalizationListener listener = this::fire;
  private boolean connected = false;

  public LocalizationProviderBridge(ILocalizationProvider provider) {
    this.provider = provider;
    connect();
  }

  public void connect() {
    provider.removeLocalizationListener(listener);
    provider.addLocalizationListener(listener);
    connected = true;
  }

  public void disconnect() {
    provider.removeLocalizationListener(listener);
    connected = false;
  }

  @Override
  public String getString(String key) {
    return provider.getString(key);
  }
}
