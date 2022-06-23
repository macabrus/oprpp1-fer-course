package hr.fer.oprpp1.hw08.jnotepadpp.action;

import hr.fer.oprpp1.hw08.jnotepadpp.model.MultipleDocumentModel;
import hr.fer.oprpp1.hw08.vjezba.ILocalizationProvider;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.IOException;

public class SaveDocAction extends DocumentAction {

  public SaveDocAction(JFrame parent, MultipleDocumentModel model, String key, ILocalizationProvider lp) {
    super(parent, model, key, lp);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if(model.getCurrentDocument().getFilePath() == null) {
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
    }
    try {
      model.saveDocument(model.getCurrentDocument(), model.getCurrentDocument().getFilePath());
    } catch (IOException ioException) {
      ioException.printStackTrace();
      JOptionPane.showMessageDialog(
        parent,
        "Pogreška prilikom zapisivanja datoteke "+model.getCurrentDocument().getFilePath().toFile().getAbsolutePath(),
        "Pogreška",
        JOptionPane.ERROR_MESSAGE);
      return;
    }
    JOptionPane.showMessageDialog(
      parent,
      "Datoteka je snimljena.",
      "Informacija",
      JOptionPane.INFORMATION_MESSAGE);
  }
}
