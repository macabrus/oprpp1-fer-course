package hr.fer.zemris.java.hw06.shell;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

public class CatCommand implements ShellCommand {

  private CommandParser parser;

  public CatCommand(CommandParser parser) {
    this.parser = parser;
  }

  @Override
  public ShellStatus executeCommand(Environment env, String arguments) {
    var args = parser.parse(arguments);
    if (args.length != 2 && args.length != 1) {
      env.writeln("Expected <path_name> [, <charset_name>] as arguments.");
    }
    try (var reader = new BufferedReader(
      new InputStreamReader(
        new FileInputStream(args[0]), args.length == 2 ? Charset.forName(args[1]) : Charset.defaultCharset()
      )
    )) {
      String line;
      while ((line = reader.readLine()) != null)
        env.writeln(line);
    } catch (IOException e) {
      env.writeln("Error while reading file.");
    }
    return ShellStatus.CONTINUE;
  }
}
