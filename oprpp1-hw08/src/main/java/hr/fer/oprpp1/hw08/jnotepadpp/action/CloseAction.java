package hr.fer.oprpp1.hw08.jnotepadpp.action;

import hr.fer.oprpp1.hw08.jnotepadpp.JNotepadPP;
import hr.fer.oprpp1.hw08.jnotepadpp.model.MultipleDocumentModel;
import hr.fer.oprpp1.hw08.vjezba.ILocalizationProvider;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class CloseAction extends DocumentAction{
  private Action saveDocument;
  public CloseAction(JFrame parent, MultipleDocumentModel model, String key, ILocalizationProvider lp) {
    super(parent, model, key, lp);
    saveDocument = new SaveDocAction(parent, model, "save", lp);

  }

  @Override
  public void actionPerformed(ActionEvent e) {
    //saveDocument.actionPerformed(e);
    if (model.getCurrentDocument().isModified()){
      var res = JOptionPane.showConfirmDialog(
        parent,
        "Datoteka je modificirana. Želite li spremiti datoteku?",
        "Upozorenje",
        JOptionPane.YES_NO_CANCEL_OPTION);
      if (res == JOptionPane.YES_OPTION)
        saveDocument.actionPerformed(e);
      else if (res == JOptionPane.NO_OPTION)
        model.closeDocument(model.getCurrentDocument());
    }
    else {
      model.closeDocument(model.getCurrentDocument());
    }
  }
}
