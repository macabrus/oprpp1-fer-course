package hr.fer.oprpp1.hw08.jnotepadpp.action;

import hr.fer.oprpp1.hw08.jnotepadpp.JNotepadPP;
import hr.fer.oprpp1.hw08.jnotepadpp.model.MultipleDocumentModel;
import hr.fer.oprpp1.hw08.vjezba.ILocalizationProvider;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class OpenDocAction extends DocumentAction {

  public OpenDocAction(JFrame parent, MultipleDocumentModel model, String key, ILocalizationProvider lp) {
    super(parent, model, key, lp);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    JFileChooser fc = new JFileChooser();
    // fc.setDialogTitle("Open file");
    if(fc.showOpenDialog(parent) != JFileChooser.APPROVE_OPTION) {
      return;
    }
    File fileName = fc.getSelectedFile();
    Path filePath = fileName.toPath();
    try {
      model.loadDocument(filePath);
    } catch (IOException ioException) {
      ioException.printStackTrace();
      JOptionPane.showMessageDialog(
        parent,
        "Pogreška prilikom otvaranja datoteke!",
        "Pogreška",
        JOptionPane.ERROR_MESSAGE);
    }
  }
}
