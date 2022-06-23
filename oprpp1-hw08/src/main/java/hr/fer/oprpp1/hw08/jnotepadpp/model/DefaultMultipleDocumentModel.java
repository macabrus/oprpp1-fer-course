package hr.fer.oprpp1.hw08.jnotepadpp.model;

import hr.fer.oprpp1.hw08.vjezba.ILocalizationListener;
import hr.fer.oprpp1.hw08.vjezba.ILocalizationProvider;

import javax.swing.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class DefaultMultipleDocumentModel extends JTabbedPane implements MultipleDocumentModel, ILocalizationListener {

  private List<SingleDocumentModel> docs = new ArrayList<>();
  private SingleDocumentModel currentDoc;
  private List<MultipleDocumentListener> listeners = new ArrayList<>();
  private ILocalizationProvider l10nProvider;

  public DefaultMultipleDocumentModel(ILocalizationProvider provider) {
    l10nProvider = provider;
    provider.addLocalizationListener(this);
    addChangeListener(e -> {
      if (e.getSource() instanceof JTabbedPane) {
        JTabbedPane pane = (JTabbedPane) e.getSource();
        System.out.println("Selected doc #: " + pane.getSelectedIndex());
        setCurrentDoc(currentDoc, pane.getSelectedIndex());
      }
    });
  }

  private void setCurrentDoc(SingleDocumentModel old, int newIndex) {
    var tmp = currentDoc;
    this.currentDoc = newIndex >= 0 ? docs.get(newIndex) : null;
    setSelectedIndex(docs.indexOf(currentDoc));
    listeners.forEach(l -> l.currentDocumentChanged(tmp, currentDoc));
  }

  @Override
  public SingleDocumentModel createNewDocument() {
    var doc = new DefaultSingleDocumentModel(null, "");
    docs.add(doc);
    add(l10nProvider.getString("unnamed_doc"), new JScrollPane(doc.getTextComponent()));
    setCurrentDoc(currentDoc, docs.size() - 1); // Last added
    listeners.forEach(l -> l.documentAdded(doc));
    addModificationListener(doc);
    return doc;
  }

  @Override
  public SingleDocumentModel getCurrentDocument() {
    return currentDoc;
  }

  @Override
  public SingleDocumentModel loadDocument(Path path) throws IOException {
    var sameDoc = docs.stream()
      .filter(d -> Objects.equals(path, d.getFilePath()))
      .findFirst()
      .orElse(null);
    if (sameDoc != null){
      setCurrentDoc(getCurrentDocument(), docs.indexOf(sameDoc));
      return getCurrentDocument();
    }
    var doc = new DefaultSingleDocumentModel(path.toString(), Files.readString(path));
    docs.add(doc);
    add(doc.getFilePath().toString(), new JScrollPane(doc.getTextComponent()));
    setCurrentDoc(currentDoc, docs.size() - 1); // Last added is in focus
    listeners.forEach(l -> l.documentAdded(doc));
    addModificationListener(doc);
    return doc;
  }

  @Override
  public void saveDocument(SingleDocumentModel model, Path newPath) throws IOException {
    model.setFilePath(newPath);
    Files.writeString(newPath, model.getTextComponent().getText());
    model.setModified(false); // Setting to unmodified after save
  }

  // Model doesnt care if it is saved!!!
  // It has to be checked by caller!!!
  @Override
  public void closeDocument(SingleDocumentModel model) {
    var index = docs.indexOf(model);
    var closedDoc = docs.remove(index);
    removeTabAt(index);
    setCurrentDoc(closedDoc, docs.size() - 1);
    listeners.forEach(l -> l.documentRemoved(closedDoc));
  }

  @Override
  public void addMultipleDocumentListener(MultipleDocumentListener l) {
    listeners.add(l);
  }

  @Override
  public void removeMultipleDocumentListener(MultipleDocumentListener l) {
    listeners.remove(l);
  }

  @Override
  public int getNumberOfDocuments() {
    return docs.size();
  }

  @Override
  public SingleDocumentModel getDocument(int index) {
    return docs.get(index);
  }

  @Override
  public Iterator<SingleDocumentModel> iterator() {
    return docs.iterator();
  }

  /**
   * When localization changes,
   * component refreshes
   */
  @Override
  public void localizationChanged() {
    // This will trigger notification for localization provider
    docs.forEach(d -> d.setFilePath(d.getFilePath()));
    // for (int i = 0; i < docs.size(); i++) {
    //   var path = docs.get(i).getFilePath() != null ? docs.get(i).getFilePath().toString() : null;
    //   setTitleAt(i, path == null ? l10nProvider.getString("unnamed_doc") : path);
    // }
  }

  /**
   * Attaches listener to every Single document submodel
   */
  private void addModificationListener(SingleDocumentModel doc) {
    doc.getTextComponent().addCaretListener(e -> listeners.forEach(l -> l.currentDocumentChanged(currentDoc, currentDoc)));
    doc.addSingleDocumentListener(new SingleDocumentListener() {

      @Override
      public void documentModifyStatusUpdated(SingleDocumentModel model) {
        if (model.isModified())
          setIconAt(docs.indexOf(model), UIManager.getIcon("FileView.floppyDriveIcon"));
        else
          setIconAt(docs.indexOf(model), null);
      }

      @Override
      public void documentFilePathUpdated(SingleDocumentModel model) {
        if (model.getFilePath() == null)
          setTitleAt(docs.indexOf(model), l10nProvider.getString("unnamed_doc"));
        else
          setTitleAt(docs.indexOf(model), model.getFilePath().toString());
      }
    });
  }

}
