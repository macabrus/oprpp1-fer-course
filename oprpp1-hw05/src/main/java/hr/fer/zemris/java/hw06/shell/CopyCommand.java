package hr.fer.zemris.java.hw06.shell;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class CopyCommand implements ShellCommand {

  private CommandParser parser;

  public CopyCommand(CommandParser parser) {
    this.parser = parser;
  }

  @Override
  public ShellStatus executeCommand(Environment env, String arguments) {
    var args = parser.parse(arguments);
    if (args.length != 2){
      env.writeln("Expected two arguments <source_path> <destination_path>.");
      return ShellStatus.CONTINUE;
    }
    try {
      Files.copy(Path.of(args[0]), Path.of(args[1]));
    } catch (IOException e) {
      env.writeln("Error while copying file.");
      e.printStackTrace();
    }
    return ShellStatus.CONTINUE;
  }
}
