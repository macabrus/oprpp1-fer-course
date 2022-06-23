package hr.fer.zemris.java.gui.prime;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PrimeDemo {

  private static PrimeListModel m = new PrimeListModelImpl();

  public static void main(String[] args) {
    // two example windows
    SwingUtilities.invokeLater(PrimeDemo::genWindow);
    SwingUtilities.invokeLater(PrimeDemo::genWindow);
  }

  private static JFrame genWindow() {
    var jf = new JFrame();
    var cp = jf.getContentPane();
    cp.setLayout(new BoxLayout(cp, BoxLayout.PAGE_AXIS));
    // on click, trigger model to generate next prime
    cp.add(new JButton("Next") {{
      addMouseListener(new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
          m.next(e.getSource());
        }
      });
      setAlignmentX(Component.LEFT_ALIGNMENT);
    }});
    // automatically scrolling to bottom
    // using the same model for both windows as required
    cp.add(new JScrollPane(new JPrimeList(m)) {{
      getVerticalScrollBar().addAdjustmentListener(
        e -> {
          Adjustable adjustable = e.getAdjustable();
          adjustable.setValue(adjustable.getMaximum());
        });
    }});
    jf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    jf.pack();
    jf.setSize(300,200);
    jf.setVisible(true);
    return jf;
  }
}
