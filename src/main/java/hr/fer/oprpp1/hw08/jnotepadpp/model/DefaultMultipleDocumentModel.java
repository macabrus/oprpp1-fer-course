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

public class DefaultMultipleDocumentModel extends JTabbedPane implements MultipleDocumentModel, ILocalizationListener {

  private List<SingleDocumentModel> docs = new ArrayList<>();
  private SingleDocumentModel currentDoc;
  private List<MultipleDocumentListener> listeners = new ArrayList<>();
  private ILocalizationProvider l10nProvider;

  public DefaultMultipleDocumentModel(ILocalizationProvider provider) {
    l10nProvider = provider;
    provider.addLocalizationListener(this);
  }

  @Override
  public SingleDocumentModel createNewDocument() {
    var doc = new DefaultSingleDocumentModel(null, "");
    docs.add(doc);
    add(l10nProvider.getString("unnamed_doc"), new JScrollPane(doc.getTextComponent()));
    setCurrentDoc(doc);
    listeners.forEach(l -> l.documentAdded(doc));
    return doc;
  }

  @Override
  public SingleDocumentModel getCurrentDocument() {
    return currentDoc;
  }

  @Override
  public SingleDocumentModel loadDocument(Path path) throws IOException {
    var doc = new DefaultSingleDocumentModel(path.toString(), Files.readString(path));
    docs.add(doc);
    add(doc.getFilePath().toString(), new JScrollPane(doc.getTextComponent()));
    setCurrentDoc(doc);
    listeners.forEach(l -> l.documentAdded(doc));
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
    docs.remove(index);
    removeTabAt(index);
    if (!docs.isEmpty())
      setCurrentDoc(docs.get(docs.size()-1));
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

  @Override
  public void localizationChanged() {
    for (int i = 0; i < docs.size(); i++) {
      var path = docs.get(i).getFilePath() != null ? docs.get(i).getFilePath().toString() : null;
      setTitleAt(i, path == null ? l10nProvider.getString("unnamed_doc") : path);
    }
  }

  public void setCurrentDoc(SingleDocumentModel currentDoc) {
    this.currentDoc = currentDoc;
    setSelectedIndex(docs.indexOf(currentDoc));
  }

}
