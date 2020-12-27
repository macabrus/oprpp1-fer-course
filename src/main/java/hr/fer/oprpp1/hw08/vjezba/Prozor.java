package hr.fer.oprpp1.hw08.vjezba;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Prozor extends JFrame {

  private static final long serialVersionUID = 1L;
  private FormLocalizationProvider flp = new FormLocalizationProvider(LocalizationProvider.getInstance(), this);

  public Prozor() throws HeadlessException {
    setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    setLocation(0, 0);
    setTitle("Demo");
    initGUI();
    pack();
  }

  private void initGUI() {
    getContentPane().setLayout(new BorderLayout());

    JButton gumb = new JButton(new UnitLocalizableAction("login", flp));

    // Change lang on click adapter
    var adapter = new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        LocalizationProvider.getInstance().setLanguage(((JMenuItem)e.getSource()).getText());
        System.out.println("Detected");
      }
    };

    // Adding lang change buttons to toolbar
    getContentPane().add(new JMenuBar(){{
      add(new JMenu("Languages") {{
        add(new JMenuItem("en") {{ addActionListener(adapter);}});
        add(new JMenuItem("hr") {{ addActionListener(adapter);}});
        add(new JMenuItem("de") {{ addActionListener(adapter);}});
      }});
    }}, BorderLayout.NORTH);
    // Adding localized button component that should change language on event
    getContentPane().add(gumb, BorderLayout.CENTER);

    flp.addLocalizationListener(() -> gumb.setText(LocalizationProvider.getInstance().getString("login")));
  }

  public static void main(String[] args) {
    if (args.length != 1) {
      System.err.println("Očekivao sam oznaku jezika kao argument!");
      System.err.println("Zadajte kao parametar hr ili en.");
      System.exit(-1);
    }
    final String jezik = args[0];
    SwingUtilities.invokeLater(() -> {
      LocalizationProvider.getInstance().setLanguage(jezik);
      new Prozor().setVisible(true);
    });
  }
}
