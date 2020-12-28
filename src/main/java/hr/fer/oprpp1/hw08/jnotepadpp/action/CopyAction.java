package hr.fer.oprpp1.hw08.jnotepadpp.action;

import hr.fer.oprpp1.hw08.jnotepadpp.model.MultipleDocumentModel;
import hr.fer.oprpp1.hw08.vjezba.ILocalizationProvider;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class CopyAction extends DocumentAction{
  public CopyAction(JFrame parent, MultipleDocumentModel model, String key, ILocalizationProvider lp) {
    super(parent, model, key, lp);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    model.getCurrentDocument().getTextComponent().copy();
  }
}
