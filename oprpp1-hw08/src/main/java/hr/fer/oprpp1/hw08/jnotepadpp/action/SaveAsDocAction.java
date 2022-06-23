package hr.fer.oprpp1.hw08.jnotepadpp.action;

import hr.fer.oprpp1.hw08.jnotepadpp.JNotepadPP;
import hr.fer.oprpp1.hw08.jnotepadpp.model.MultipleDocumentModel;
import hr.fer.oprpp1.hw08.vjezba.ILocalizationProvider;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class SaveAsDocAction extends DocumentAction {

  private Action saveDocument;

  public SaveAsDocAction(JFrame parent, MultipleDocumentModel model, String key, ILocalizationProvider lp) {
    super(parent, model, key, lp);
    saveDocument = new SaveDocAction(parent, model, "save", lp);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    JFileChooser jfc = new JFileChooser();
    jfc.setDialogTitle("Save document");
    if(jfc.showSaveDialog(parent) != JFileChooser.APPROVE_OPTION) {
      JOptionPane.showMessageDialog(
        parent,
        "Ništa nije snimljeno.",
        "Upozorenje",
        JOptionPane.WARNING_MESSAGE);
      return;
    }
    model.getCurrentDocument().setFilePath(jfc.getSelectedFile().toPath());
    saveDocument.actionPerformed(null); // Now that it's saved
  }
}
