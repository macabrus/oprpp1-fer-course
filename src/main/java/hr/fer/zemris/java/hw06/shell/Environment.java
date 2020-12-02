package hr.fer.zemris.java.hw06.shell;

import java.util.SortedMap;

public interface Environment {
  String readLine() throws ShellIOException;
  void write(String text) throws ShellIOException;
  void writeln(String text) throws ShellIOException;
  SortedMap<String, ShellCommand> commands();
  Character getMultilineContinuationPrompt();
  void setMultilineChar(Character symbol);

  void setMultilineSymbol(Character symbol);
  String getPromptSymbol();
  void setPromptSymbol(String symbol);
  Character getMultilineChar();
}
