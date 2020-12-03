package hr.fer.zemris.java.hw06.shell;

public class HelpCommand implements ShellCommand {

  private CommandParser parser;

  public HelpCommand(CommandParser parser) {
    this.parser = parser;
  }

  @Override
  public ShellStatus executeCommand(Environment env, String arguments) {
    var args = parser.parse(arguments);
    if (args.length > 1) {
      env.writeln("Expected [, <file_path>] as an argument.");
    }
    if (args.length == 0) {
      env.writeln("Available commands are:");
      env.commands().forEach((k, v) -> env.writeln("  " + k));
    }
    else {
      env.writeln("Name:");
      env.writeln("  " + env.commands().get(args[0]).getCommandName());
      env.writeln("Description:");
      env.commands().get(args[0]).getCommandDescription().stream().map(l -> "  " + l).forEach(env::writeln);
    }
    return ShellStatus.CONTINUE;
  }
}
