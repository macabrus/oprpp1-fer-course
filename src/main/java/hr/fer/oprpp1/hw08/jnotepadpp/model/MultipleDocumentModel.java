package hr.fer.oprpp1.hw08.jnotepadpp.model;

import java.io.IOException;
import java.nio.file.Path;

public interface MultipleDocumentModel extends Iterable<SingleDocumentModel> {
  SingleDocumentModel createNewDocument();
  SingleDocumentModel getCurrentDocument();
  SingleDocumentModel loadDocument(Path path) throws IOException;
  void saveDocument(SingleDocumentModel model, Path newPath) throws IOException;
  void closeDocument(SingleDocumentModel model);
  void addMultipleDocumentListener(MultipleDocumentListener l);
  void removeMultipleDocumentListener(MultipleDocumentListener l);
  int getNumberOfDocuments();
  SingleDocumentModel getDocument(int index); }
