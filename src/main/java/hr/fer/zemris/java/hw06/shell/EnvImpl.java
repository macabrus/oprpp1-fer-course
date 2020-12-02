package hr.fer.zemris.java.hw06.shell;

import java.io.IOException;
import java.util.Scanner;
import java.util.SortedMap;

public class EnvImpl implements Environment{

  private String PROMPTSYMBOL = " > ";
  private String MULTILINESYMBOL = ""

  @Override
  public String readLine() throws ShellIOException {
    var sc = new Scanner(in);
    var sb = new StringBuilder();
    while (true) {
      var part = sc.nextLine();
      if (part.endsWith(String.valueOf(getMultilineContinuationPrompt()))) {
        sb.append(part.replaceFirst("/\\s*$", ""));
      }
      else
        break;
    }
    return sb.toString();
  }

  @Override
  public void write(String text) throws ShellIOException {
    try {
      out.write(text.getBytes());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void writeln(String text) throws ShellIOException {
    try {
      out.write((text + '\n').getBytes());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public SortedMap<String, ShellCommand> commands() {
    return commands;
  }

  @Override
  public String getMultilineContinuationPrompt() {
    return multilineChar;
  }

  @Override
  public void setMultilineContinuationPrompt(String symbol) {
    multilineChar = symbol;
  }

  @Override
  public String getPromptSymbol() {
    return prompt;
  }

  @Override
  public void setPromptSymbol(String symbol) {
    this.prompt = symbol;
  }

  @Override
  public Character getMultilineChar() {
    return multilineChar;
  }

  @Override
  public void setMultilineChar(Character symbol) {
    multilineChar = symbol;
  }
}
