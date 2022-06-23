package hr.fer.oprpp1.hw08.jnotepadpp.action;

import hr.fer.oprpp1.hw08.jnotepadpp.model.MultipleDocumentModel;
import hr.fer.oprpp1.hw08.vjezba.ILocalizationProvider;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class ToggleCaseAction extends DocumentAction{
  public ToggleCaseAction(JFrame parent, MultipleDocumentModel model, String key, ILocalizationProvider lp) {
    super(parent, model, key, lp);
  }

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
}
