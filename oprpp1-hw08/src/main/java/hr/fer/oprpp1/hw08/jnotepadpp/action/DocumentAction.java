package hr.fer.oprpp1.hw08.jnotepadpp.action;

import hr.fer.oprpp1.hw08.jnotepadpp.model.MultipleDocumentModel;
import hr.fer.oprpp1.hw08.vjezba.ILocalizationProvider;
import hr.fer.oprpp1.hw08.vjezba.LocalizableAction;

import javax.swing.*;

public abstract class DocumentAction extends LocalizableAction {

  protected final JFrame parent;
  protected final MultipleDocumentModel model;

  public DocumentAction(JFrame parent, MultipleDocumentModel model, String key, ILocalizationProvider lp) {
    super(key, lp);
    this.parent = parent;
    this.model = model;
  }

}
