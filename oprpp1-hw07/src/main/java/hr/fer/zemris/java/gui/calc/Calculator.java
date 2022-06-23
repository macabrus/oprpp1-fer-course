package hr.fer.zemris.java.gui.calc;

import hr.fer.zemris.java.gui.layouts.CalcLayout;
import hr.fer.zemris.java.gui.layouts.RCPosition;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public class Calculator extends JFrame {

  public Calculator() {
    setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    initGUI();
    pack();
  }

  private CalcModel model = new CalcModelImpl();

  private void initGUI() {
    Container cp = getContentPane();cp.setLayout(new CalcLayout(3));

    // 1. red
    cp.add(new Display(model), new RCPosition(1,1));
    cp.add(btn("="), new RCPosition(1,6));
    cp.add(btn("clr"), new RCPosition(1,7));
    // 2. red
    cp.add(btn("1/x"), new RCPosition(2,1));
    cp.add(btn("sin"), new RCPosition(2,2));
    cp.add(num("7"), new RCPosition(2,3));
    cp.add(num("8"), new RCPosition(2,4));
    cp.add(num("9"), new RCPosition(2,5));
    cp.add(btn("/"), new RCPosition(2,6));
    cp.add(btn("res"), new RCPosition(2,7));
    // 3. red
    cp.add(btn("log"), new RCPosition(3,1));
    cp.add(btn("cos"), new RCPosition(3,2));
    cp.add(num("4"), new RCPosition(3,3));
    cp.add(num("5"), new RCPosition(3,4));
    cp.add(num("6"), new RCPosition(3,5));
    cp.add(btn("*"), new RCPosition(3,6));
    cp.add(btn("push"), new RCPosition(3,7));
    // 4. red
    cp.add(btn("ln"), new RCPosition(4,1));
    cp.add(btn("tan"), new RCPosition(4,2));
    cp.add(num("1"), new RCPosition(4,3));
    cp.add(num("2"), new RCPosition(4,4));
    cp.add(num("3"), new RCPosition(4,5));
    cp.add(btn("-"), new RCPosition(4,6));
    cp.add(btn("pop"), new RCPosition(4,7));
    // 5. red
    cp.add(btn("x^n"), new RCPosition(5,1));
    cp.add(btn("ctg"), new RCPosition(5,2));
    cp.add(num("0"), new RCPosition(5,3));
    cp.add(btn("+/-"), new RCPosition(5,4));
    cp.add(num("."), new RCPosition(5,5));
    cp.add(btn("+"), new RCPosition(5,6));
    var checkbox = new JCheckBox();
    checkbox.addItemListener(evt -> model.useInverse(evt.getStateChange() == ItemEvent.SELECTED));
    cp.add(checkbox, new RCPosition(5,7));

  }

  /**
   * Creates common styled button for calculator
   * @param txt
   * @return
   */
  private JLabel btn(String txt) {
    JLabel l = new JLabel(txt, JLabel.CENTER);
    l.setBackground(Color.decode("#DDDDFF"));
    l.setBorder(BorderFactory.createLineBorder(Color.decode("#7A8A99"),1));
    l.setOpaque(true);
    makeDarkOnClick(l);
    l.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        model.perform(((JLabel) e.getComponent()).getText());
      }
    });
    return l;
  }

  /**
   * Digit button type (types digit into model)
   * @param num
   * @return
   */
  private JLabel num(String num) {
    JLabel l = btn(num);
    l.setFont(l.getFont().deriveFont(30f));
    return l;
  }

  // makes component darker when pressed
  private void makeDarkOnClick(Component c) {
    c.addMouseListener(new MouseAdapter() {
      @Override
      public void mousePressed(MouseEvent e) {
        c.setBackground(Color.decode("#DDDDFF").darker());
      }

      @Override
      public void mouseReleased(MouseEvent e) {
        c.setBackground(Color.decode("#DDDDFF"));
      }
    });
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> new Calculator().setVisible(true));
  }
}
