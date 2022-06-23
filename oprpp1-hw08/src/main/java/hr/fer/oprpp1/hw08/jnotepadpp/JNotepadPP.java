package hr.fer.oprpp1.hw08.jnotepadpp;

import hr.fer.oprpp1.hw08.jnotepadpp.action.*;
import hr.fer.oprpp1.hw08.jnotepadpp.component.StatusLabel;
import hr.fer.oprpp1.hw08.jnotepadpp.model.DefaultMultipleDocumentModel;
import hr.fer.oprpp1.hw08.jnotepadpp.model.MultipleDocumentAdapter;
import hr.fer.oprpp1.hw08.jnotepadpp.model.MultipleDocumentModel;
import hr.fer.oprpp1.hw08.jnotepadpp.model.SingleDocumentModel;
import hr.fer.oprpp1.hw08.vjezba.FormLocalizationProvider;
import hr.fer.oprpp1.hw08.vjezba.LocalizationProvider;
import hr.fer.oprpp1.hw08.vjezba.UnitLocalizableAction;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Utilities;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.Collator;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Locale;

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
    setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
        exit.actionPerformed(null);
      }
    });
    initGUI();
    setSize(800, 600);
    wireActions();
  }

  private void initGUI() {


    model.addMultipleDocumentListener(new MultipleDocumentAdapter() {
      @Override
      public void currentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel) {
        if (model.getNumberOfDocuments() == 0)
          setTitle("JNotepad++");
        else if (model.getCurrentDocument().getFilePath() == null)
          setTitle(flp.getString("unnamed_doc") + " - JNotepad++");
        else
          setTitle(model.getCurrentDocument().getFilePath() + " - JNotepad++");
      }
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
    edit.add(new JMenu(new UnitLocalizableAction("change_case", flp)){{
      add(new JMenuItem(toggleCase));
      add(new JMenuItem(uppercase));
      add(new JMenuItem(lowercase));
    }});
    edit.add(new JMenu(new UnitLocalizableAction("sort", flp)) {{
      add(new JMenuItem(sortAsc));
      add(new JMenuItem(sortDesc));
    }});
    edit.add(new JMenuItem(copySelected));
    edit.add(new JMenuItem(paste));
    edit.add(new JMenuItem(cutSelected));
    var lang = new JMenu(new UnitLocalizableAction("lang", flp));
    lang.add(new JMenuItem("HR") {{
      addActionListener(e -> LocalizationProvider.getInstance().setLanguage("hr"));
    }});
    lang.add(new JMenuItem("EN") {{
      addActionListener(e -> LocalizationProvider.getInstance().setLanguage("en"));
    }});
    lang.add(new JMenuItem("DE") {{
      addActionListener(e -> LocalizationProvider.getInstance().setLanguage("de"));
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

    // Status bar
    getContentPane().add(new JToolBar() {{
      var len = new StatusLabel("len", flp);
      var ln = new StatusLabel("ln", flp);
      var col = new StatusLabel("col", flp);
      var sel = new StatusLabel("sel", flp);
      var border = BorderFactory.createEmptyBorder(5,5,5,5);
      len.setBorder(border);
      ln.setBorder(border);
      col.setBorder(border);
      sel.setBorder(border);
      add(len);
      add(new JSeparator());
      add(ln);
      add(col);
      add(sel);
      add(new JLabel() {{
        setBorder(border);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss").withZone(ZoneId.systemDefault());
        var t = new Timer(1000, e -> setText(String.valueOf(formatter.format(Instant.now()))));
        t.start();
        JNotepadPP.this.addWindowListener(new WindowAdapter() {
          @Override
          public void windowClosed(WindowEvent e) {
            System.out.println("Stopping timer");
            t.stop();
          }
        });
      }});
      model.addMultipleDocumentListener(new MultipleDocumentAdapter() {

        @Override
        public void currentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel) {
          if (currentModel == null) {
            len.setValue("");
            ln.setValue("");
            col.setValue("");
            sel.setValue("");
            return;
          }
          var area = currentModel.getTextComponent();
          len.setValue(String.valueOf(area.getText().length()));
          int caretPos = area.getCaretPosition();
          int rowNum = (caretPos == 0) ? 1 : 0;
          for (int offset = caretPos; offset > 0;) {
            try {
              offset = Utilities.getRowStart(area, offset) - 1;
            } catch (BadLocationException e) {
              e.printStackTrace();
              break;
            }
            rowNum++;
          }
          int offset = 0;
          try {
            offset = Utilities.getRowStart(area, caretPos);
          } catch (BadLocationException e) {
            e.printStackTrace();
          }
          int colNum = caretPos - offset + 1;
          ln.setValue(String.valueOf(rowNum));
          col.setValue(String.valueOf(colNum));
          sel.setValue(String.valueOf((area.getSelectionEnd() - area.getSelectionStart())));
        }
      });
    }}, BorderLayout.SOUTH);

    pack();

  }

  /**
   * Wires different methods of invoking action to
   * specific action.
   */
  private void wireActions() {
    newDocument.putValue(
      Action.ACCELERATOR_KEY,
      KeyStroke.getKeyStroke("control N"));
    newDocument.putValue(
      Action.MNEMONIC_KEY,
      KeyEvent.VK_N);
    newDocument.putValue(
      Action.SHORT_DESCRIPTION,
      "Used to create new document.");


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
    // If document is already saved, disable save
    model.addMultipleDocumentListener(new MultipleDocumentAdapter() {
      @Override
      public void currentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel) {
        saveDocument.setEnabled(currentModel != null && currentModel.isModified());
      }
    });

    deleteSelected.putValue(
      Action.ACCELERATOR_KEY,
      KeyStroke.getKeyStroke("F2"));
    deleteSelected.putValue(
      Action.MNEMONIC_KEY,
      KeyEvent.VK_D);
    deleteSelected.putValue(
      Action.SHORT_DESCRIPTION,
      "Used to delete the selected part of text.");
    model.addMultipleDocumentListener(new MultipleDocumentAdapter() {
      @Override
      public void currentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel) {
        var notNull = currentModel != null && currentModel.getTextComponent().getSelectedText() != null;
        deleteSelected.setEnabled(notNull);
        toggleCase.setEnabled(notNull);
        uppercase.setEnabled(notNull);
        lowercase.setEnabled(notNull);
        cutSelected.setEnabled(notNull);
        copySelected.setEnabled(notNull);
      }
    });

    toggleCase.putValue(
      Action.ACCELERATOR_KEY,
      KeyStroke.getKeyStroke("control F3"));
    toggleCase.putValue(
      Action.MNEMONIC_KEY,
      KeyEvent.VK_T);
    toggleCase.putValue(
      Action.SHORT_DESCRIPTION,
      "Used to toggle character case in selected part of text or in entire document.");

    closeDocument.putValue(
      Action.ACCELERATOR_KEY,
      KeyStroke.getKeyStroke("control W"));
    closeDocument.putValue(
      Action.MNEMONIC_KEY,
      KeyEvent.VK_W);
    closeDocument.putValue(
      Action.SHORT_DESCRIPTION,
      "Used to create new document.");

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

  private final Action newDocument = new NewDocAction(this, model, "new", flp);
  private final Action openDocument = new OpenDocAction(this, model, "open", flp);
  private final Action saveDocument = new SaveDocAction(this, model, "save", flp);
  private final Action deleteSelected = new DeleteSelectionAction(this, model, "delete", flp);

  private final Action toggleCase = new ToggleCaseAction(this, model, "toggle_case", flp);
  private final Action cutSelected = new CutAction(this, model, "cut", flp);
  private final Action copySelected = new CopyAction(this, model, "copy", flp);
  private final Action paste = new PasteAction(this, model, "paste", flp);
  private final Action uppercase = new UppercaseAction(this, model, "upper", flp);
  private final Action lowercase = new LowercaseAction(this, model, "lower", flp);
  private final Action exit = new ExitAction(this, model, "exit", flp);
  private final Action saveAsDocument = new SaveAsDocAction(this, model, "save_as", flp);
  private final Action closeDocument = new CloseAction(this,model, "close", flp);
  private final Action sortAsc = new SortAction(this, model, "asc", flp) {
    @Override
    public Comparator<String> provideComparator() {
      return new Comparator<>() {
        private final Collator c = Collator.getInstance(Locale.forLanguageTag(LocalizationProvider.getInstance().getLanguage()));

        @Override
        public int compare(String o1, String o2) {
          return c.compare(o1, o2);
        }
      };
    }
  };
  private final Action sortDesc = new SortAction(this, model, "desc", flp) {
    @Override
    public Comparator<String> provideComparator() {
      return new Comparator<>() {
        private final Comparator<Object> c = Collator.getInstance(Locale.forLanguageTag(LocalizationProvider.getInstance().getLanguage())).reversed();

        @Override
        public int compare(String o1, String o2) {
          return c.compare(o1, o2);
        }
      };
    }
  };

}
