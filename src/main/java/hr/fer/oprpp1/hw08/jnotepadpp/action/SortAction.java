package hr.fer.oprpp1.hw08.jnotepadpp.action;

import hr.fer.oprpp1.hw08.jnotepadpp.model.MultipleDocumentModel;
import hr.fer.oprpp1.hw08.vjezba.ILocalizationProvider;
import hr.fer.oprpp1.hw08.vjezba.LocalizationProvider;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.event.ActionEvent;
import java.text.Collator;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Locale;

public abstract class SortAction extends DocumentAction {

  public SortAction(JFrame parent, MultipleDocumentModel model, String key, ILocalizationProvider lp) {
    super(parent, model, key, lp);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    var content = model.getCurrentDocument().getTextComponent().getText().split(System.lineSeparator());
    var area = model.getCurrentDocument().getTextComponent();
    try {
      var lineStart = area.getText(0, area.getSelectionStart()).split(System.lineSeparator()).length - 1;
      var lineEnd = area.getText(0, area.getSelectionEnd() - 1).split(System.lineSeparator()).length;
      var collator = provideComparator();
      Arrays.sort(content, lineStart, lineEnd, collator);
      System.out.println("LINE START: " + lineStart);
      System.out.println("LINE END: " + lineEnd);
      area.setText(String.join(System.lineSeparator(), content));
    } catch (BadLocationException badLocationException) {
      badLocationException.printStackTrace();
    }
  }

  public abstract Comparator<String> provideComparator();
}
