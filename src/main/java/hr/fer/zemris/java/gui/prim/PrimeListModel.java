package hr.fer.zemris.java.gui.prim;

import javax.swing.*;

public interface PrimeListModel extends ListModel<Integer> {
  int next(Object source);
}
