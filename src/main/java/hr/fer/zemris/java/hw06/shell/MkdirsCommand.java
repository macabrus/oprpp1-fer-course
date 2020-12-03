package hr.fer.zemris.java.hw06.shell;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class MkdirsCommand implements ShellCommand {

  private CommandParser parser;

  public MkdirsCommand(CommandParser parser) {
    this.parser = parser;
  }

  @Override
  public ShellStatus executeCommand(Environment env, String arguments) {
    var args = parser.parse(arguments);
    if (args.length != 1){
      env.writeln("Expected single arguments <dir_name>.");
      return ShellStatus.CONTINUE;
    }
    new File(args[0]).mkdirs();
    return ShellStatus.CONTINUE;
  }
}
