package hr.fer.zemris.java.gui.prime;

import javax.swing.*;

public interface PrimeListModel extends ListModel<Integer> {
  int next(Object source);
}
