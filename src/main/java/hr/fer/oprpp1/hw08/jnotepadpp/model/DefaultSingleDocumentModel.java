package hr.fer.oprpp1.hw08.jnotepadpp.model;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class DefaultSingleDocumentModel implements SingleDocumentModel {

  private Path path;
  JTextArea area = new JTextArea();
  private boolean modified;
  private List<SingleDocumentListener> listeners = new ArrayList<>();

  public DefaultSingleDocumentModel(String path, String content) {
    if (path != null)
      this.path = Path.of(path);
    area.setText(content);
    // Registering modification listener
    area.getDocument().addDocumentListener(new DocumentListener() {

      private void update(DocumentEvent e) {
        setModified(true);
      }

      @Override
      public void insertUpdate(DocumentEvent e) {
        update(e);
      }

      @Override
      public void removeUpdate(DocumentEvent e) {
        update(e);
      }

      @Override
      public void changedUpdate(DocumentEvent e) {
        update(e);
      }
    });
  }

  @Override
  public JTextArea getTextComponent() {
    return area;
  }

  @Override
  public Path getFilePath() {
    return path;
  }

  @Override
  public void setFilePath(Path path) {
    if (path == null)
      throw new NullPointerException("File path can not be null!");

    this.path = path;
  }

  @Override
  public boolean isModified() {
    return modified;
  }

  @Override
  public void setModified(boolean modified) {
    this.modified = modified;
    listeners.forEach(l -> l.documentModifyStatusUpdated(this));
  }

  @Override
  public void addSingleDocumentListener(SingleDocumentListener l) {
    listeners.add(l);
  }

  @Override
  public void removeSingleDocumentListener(SingleDocumentListener l) {
    listeners.remove(l);
  }
}
