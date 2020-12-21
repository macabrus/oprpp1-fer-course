package hr.fer.zemris.java.gui.prim;

import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import java.util.ArrayList;
import java.util.List;

public class PrimeListModelImpl implements PrimeListModel {

  private List<Integer> primes = new ArrayList<>() {{
    add(2);
  }};

  private List<ListDataListener> observers = new ArrayList<>();


  // Eratostenovo sito
  @Override
  public int next(Object source) {
    int num = primes.get(primes.size() - 1) + 1;
    double sqrt = Math.sqrt(num);
    int i = 0;
    while ( true ) {
      if (primes.get(i) > sqrt) {
        break;
      }
      else if (num % primes.get(i) == 0) {
        i = 0;
        num++;
        sqrt = Math.sqrt(num);
      }
      else
        i++;
    }
    primes.add(num);
    observers.forEach(o -> o.intervalAdded(new ListDataEvent(source, ListDataEvent.CONTENTS_CHANGED, 0, primes.size())));
    return num;
  }

  @Override
  public int getSize() {
    return primes.size();
  }

  @Override
  public Integer getElementAt(int index) {
    return primes.get(index);
  }

  @Override
  public void addListDataListener(ListDataListener l) {
    observers.add(l);
  }

  @Override
  public void removeListDataListener(ListDataListener l) {
    observers.remove(l);
  }
}
