package hr.fer.oprpp1.hw08.jnotepadpp;

import hr.fer.oprpp1.hw08.jnotepadpp.model.DefaultMultipleDocumentModel;
import hr.fer.oprpp1.hw08.jnotepadpp.model.MultipleDocumentModel;
import hr.fer.oprpp1.hw08.vjezba.FormLocalizationProvider;
import hr.fer.oprpp1.hw08.vjezba.LocalizableAction;
import hr.fer.oprpp1.hw08.vjezba.LocalizationProvider;
import hr.fer.oprpp1.hw08.vjezba.UnitLocalizableAction;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class JNotepadPP extends JFrame {

  private final FormLocalizationProvider flp = new FormLocalizationProvider(LocalizationProvider.getInstance(), this);
  private final MultipleDocumentModel model = new DefaultMultipleDocumentModel(flp);

  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> new JNotepadPP().setVisible(true));
  }

  public JNotepadPP() {
    // try {
    //   UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
    // } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
    //   e.printStackTrace();
    // }
    setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    // setLocation(0, 0);
    setSize(800, 600);
    initGUI();
    wireActions();
  }

  private void initGUI() {

    flp.addLocalizationListener(() -> {
      if (model.getNumberOfDocuments() == 0)
        setTitle("JNotepad++");
      else if (model.getCurrentDocument().getFilePath() == null)
        setTitle(flp.getString("unnamed_doc") + " - JNotepad++");
      else
        setTitle(model.getCurrentDocument().getFilePath() + " - JNotepad++");
    });

    this.getContentPane().setLayout(new BorderLayout());
    var file = new JMenu(new UnitLocalizableAction("file", flp));
    file.add(new JMenuItem(newDocument));
    file.add(new JMenuItem(openDocument));
    file.add(new JMenuItem(saveDocument));
    file.add(new JMenuItem(saveAsDocument));
    file.add(new JSeparator());
    file.add(new JMenuItem(closeDocument));
    file.add(new JMenuItem(exit));

    var edit = new JMenu(new UnitLocalizableAction("edit", flp));
    edit.add(new JMenuItem(cutSelected));
    edit.add(new JMenuItem(copySelected));
    edit.add(new JMenuItem(paste));

    var lang = new JMenu(new UnitLocalizableAction("lang", flp));
    lang.add(new JMenuItem("HR") {{
      addActionListener(e -> LocalizationProvider.getInstance().setLanguage("hr"));
    }});
    lang.add(new JMenuItem("EN") {{
      addActionListener(e -> LocalizationProvider.getInstance().setLanguage("en"));
    }});

    // var panel = new JPanel();
    // panel.setLayout(new BoxLayout(panel,BoxLayout.PAGE_AXIS));
    // getContentPane().add(new JMenuBar() {{
    //   add(file);
    //   add(edit);
    //   add(lang);
    // }});
    // panel.add(new JToolBar() {{
    //   add(new JButton(deleteSelection));
    //   add(new JButton(toggleCase));
    // }});

    // Adding panel with menu bar and toolbar
    getContentPane().add(new JMenuBar() {{
        add(file);
        add(edit);
        add(lang);
      }}, BorderLayout.NORTH);

    model.createNewDocument();
    // Adding Tabbed panel in center
    getContentPane().add((DefaultMultipleDocumentModel) model, BorderLayout.CENTER);

    pack();

  }

  /**
   * Wires different methods of invoking action to
   * specific action.
   */
  private void wireActions() {
    openDocument.putValue(
      Action.ACCELERATOR_KEY,
      KeyStroke.getKeyStroke("control O"));
    openDocument.putValue(
      Action.MNEMONIC_KEY,
      KeyEvent.VK_O);
    openDocument.putValue(
      Action.SHORT_DESCRIPTION,
      "Used to open existing file from disk.");

    saveDocument.putValue(
      Action.ACCELERATOR_KEY,
      KeyStroke.getKeyStroke("control S"));
    saveDocument.putValue(
      Action.MNEMONIC_KEY,
      KeyEvent.VK_S);
    saveDocument.putValue(
      Action.SHORT_DESCRIPTION,
      "Used to save current file to disk.");

    deleteSelection.putValue(
      Action.ACCELERATOR_KEY,
      KeyStroke.getKeyStroke("F2"));
    deleteSelection.putValue(
      Action.MNEMONIC_KEY,
      KeyEvent.VK_D);
    deleteSelection.putValue(
      Action.SHORT_DESCRIPTION,
      "Used to delete the selected part of text.");

    toggleCase.putValue(
      Action.ACCELERATOR_KEY,
      KeyStroke.getKeyStroke("control F3"));
    toggleCase.putValue(
      Action.MNEMONIC_KEY,
      KeyEvent.VK_T);
    toggleCase.putValue(
      Action.SHORT_DESCRIPTION,
      "Used to toggle character case in selected part of text or in entire document.");


    exit.putValue(
      Action.ACCELERATOR_KEY,
      KeyStroke.getKeyStroke("control X"));
    exit.putValue(
      Action.MNEMONIC_KEY,
      KeyEvent.VK_X);
    exit.putValue(
      Action.SHORT_DESCRIPTION,
      "Exit application.");
  }

  /**
   * DEFINING ACTIONS
   */

  private final Action newDocument = new LocalizableAction("new", flp) {
    @Override
    public void actionPerformed(ActionEvent e) {
      model.createNewDocument();
    }
  };
  private final Action openDocument = new LocalizableAction("open", flp) {

    @Override
    public void actionPerformed(ActionEvent e) {
      setEnabled(false);
      JFileChooser fc = new JFileChooser();
      // fc.setDialogTitle("Open file");
      if(fc.showOpenDialog(JNotepadPP.this) != JFileChooser.APPROVE_OPTION) {
        return;
      }
      File fileName = fc.getSelectedFile();
      Path filePath = fileName.toPath();
      try {
        model.loadDocument(filePath);
      } catch (IOException ioException) {
        ioException.printStackTrace();
        JOptionPane.showMessageDialog(
          JNotepadPP.this,
          "Pogreška prilikom otvaranja datoteke " + model.getCurrentDocument().getFilePath().toFile().getAbsolutePath(),
          "Pogreška",
          JOptionPane.ERROR_MESSAGE);
      }
    }
  };
  private final Action saveDocument = new LocalizableAction("save", flp) {
    @Override
    public void actionPerformed(ActionEvent e) {
      if(model.getCurrentDocument().getFilePath() == null) {
        JFileChooser jfc = new JFileChooser();
        jfc.setDialogTitle("Save document");
        if(jfc.showSaveDialog(JNotepadPP.this)!=JFileChooser.APPROVE_OPTION) {
          JOptionPane.showMessageDialog(
            JNotepadPP.this,
            "Ništa nije snimljeno.",
            "Upozorenje",
            JOptionPane.WARNING_MESSAGE);
          return;
        }
        model.getCurrentDocument().setFilePath(jfc.getSelectedFile().toPath());
      }
      try {
        model.saveDocument(model.getCurrentDocument(), model.getCurrentDocument().getFilePath());
      } catch (IOException ioException) {
        ioException.printStackTrace();
        JOptionPane.showMessageDialog(
          JNotepadPP.this,
          "Pogreška prilikom zapisivanja datoteke "+model.getCurrentDocument().getFilePath().toFile().getAbsolutePath(),
          "Pogreška",
          JOptionPane.ERROR_MESSAGE);
        return;
      }
      JOptionPane.showMessageDialog(
        JNotepadPP.this,
        "Datoteka je snimljena.",
        "Informacija",
        JOptionPane.INFORMATION_MESSAGE);
    }
  };

  private final Action deleteSelection = new LocalizableAction("delete", flp) {
    @Override
    public void actionPerformed(ActionEvent e) {
      model.getCurrentDocument().getTextComponent().replaceSelection("");
    }
  };

  private final Action toggleCase = new LocalizableAction("toggle_case", flp) {
    @Override
    public void actionPerformed(ActionEvent e) {
      model.getCurrentDocument().getTextComponent().replaceSelection(
        reverseCase(model.getCurrentDocument().getTextComponent().getSelectedText())
      );
    }

    private String reverseCase(String text)
    {
      char[] chars = text.toCharArray();
      for (int i = 0; i < chars.length; i++)
      {
        char c = chars[i];
        if (Character.isUpperCase(c))
        {
          chars[i] = Character.toLowerCase(c);
        }
        else if (Character.isLowerCase(c))
        {
          chars[i] = Character.toUpperCase(c);
        }
      }
      return new String(chars);
    }
  };
  private final Action cutSelected = new LocalizableAction("cut", flp) {
    @Override
    public void actionPerformed(ActionEvent e) {
      model.getCurrentDocument().getTextComponent().cut();
    }
  };
  private final Action copySelected = new LocalizableAction("copy", flp) {
    @Override
    public void actionPerformed(ActionEvent e) {
      model.getCurrentDocument().getTextComponent().copy();
    }
  };
  private final Action paste = new LocalizableAction("paste", flp) {
    @Override
    public void actionPerformed(ActionEvent e) {
      model.getCurrentDocument().getTextComponent().paste();
    }
  };
  private final Action exit = new LocalizableAction("exit", flp) {
    @Override
    public void actionPerformed(ActionEvent e) {
      // TODO... ask for each document to save it or discard it
    }
  };
  private final Action saveAsDocument = new LocalizableAction("save_as", flp) {
    @Override
    public void actionPerformed(ActionEvent e) {
      JFileChooser jfc = new JFileChooser();
      jfc.setDialogTitle("Save document");
      if(jfc.showSaveDialog(JNotepadPP.this) != JFileChooser.APPROVE_OPTION) {
        JOptionPane.showMessageDialog(
          JNotepadPP.this,
          "Ništa nije snimljeno.",
          "Upozorenje",
          JOptionPane.WARNING_MESSAGE);
        return;
      }
      model.getCurrentDocument().setFilePath(jfc.getSelectedFile().toPath());
      saveDocument.actionPerformed(null); // Now that it's saved
    }
  };

  private final Action closeDocument = new LocalizableAction("close", flp) {
    @Override
    public void actionPerformed(ActionEvent e) {
      // TODO, iterirat po svim dokumentima i spremit kaj nije spremljeno
    }
  };
}
