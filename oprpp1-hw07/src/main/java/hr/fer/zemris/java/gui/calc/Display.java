package hr.fer.zemris.java.gui.calc;

import javax.swing.*;
import java.awt.*;

public class Display extends JLabel implements CalcValueListener {

  public Display(CalcModel model) {
    setOpaque(true);
    setHorizontalAlignment(JLabel.RIGHT);
    setFont(getFont().deriveFont(30f));
    setBackground(Color.YELLOW);
    setBorder(BorderFactory.createLineBorder(Color.BLACK));
    model.addCalcValueListener(this);
  }

  @Override
  public void onValueChange(String txt, double val) {
    setText(txt);
  }
}
