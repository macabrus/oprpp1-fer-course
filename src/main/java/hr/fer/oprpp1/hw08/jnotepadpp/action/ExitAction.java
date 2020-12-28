package hr.fer.oprpp1.hw08.jnotepadpp.action;

import hr.fer.oprpp1.hw08.jnotepadpp.model.MultipleDocumentModel;
import hr.fer.oprpp1.hw08.vjezba.ILocalizationProvider;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class ExitAction extends DocumentAction {

  private Action closeDocument;

  public ExitAction(JFrame parent, MultipleDocumentModel model, String key, ILocalizationProvider lp) {
    super(parent, model, key, lp);
    closeDocument = new CloseAction(parent,model,"exit",lp);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    while (model.getCurrentDocument() != null) {
      var tmp = model.getCurrentDocument();
      closeDocument.actionPerformed(e);
      // If closing was ended
      if (model.getCurrentDocument() == tmp) {
        return;
      }
    }
    parent.dispose();
  }
}
