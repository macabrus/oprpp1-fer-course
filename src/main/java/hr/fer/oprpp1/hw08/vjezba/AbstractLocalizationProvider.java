package hr.fer.oprpp1.hw08.vjezba;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractLocalizationProvider implements ILocalizationProvider {

  private final List<ILocalizationListener> listeners = new ArrayList<>();

  @Override
  public void addLocalizationListener(ILocalizationListener listener) {
    listeners.add(listener);
  }

  @Override
  public void removeLocalizationListener(ILocalizationListener listener) {
    listeners.remove(listener);
  }

  public void fire() {
    listeners.forEach(ILocalizationListener::localizationChanged);
  }

}
