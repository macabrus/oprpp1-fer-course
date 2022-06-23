package hr.fer.zemris.java.hw06;

import java.util.SortedMap;

public interface Environment {
  String readLine() throws ShellIOException;
  void write(String text) throws ShellIOException;
  void writeln(String text) throws ShellIOException;
  SortedMap<String, ShellCommand> commands();
  String getMultilineSymbol();
  void setMultilineSymbol(String symbol);
  String getPromptSymbol();
  void setPromptSymbol(String symbol);
  Character getMorelinesSymbol();
  void setMorelinesSymbol(Character symbol);
  void setEnvironmentVar(String var, String value);
  String getEnvironmentVar(String var);
}
