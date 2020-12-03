package hr.fer.zemris.java.hw06.shell;

import java.util.function.Consumer;

/**
 * Modifies environment variables for given environment (or reads them).
 */
public class SymbolCommand implements ShellCommand {

  private CommandParser parser;

  public SymbolCommand(CommandParser parser) {
    this.parser = parser;
  }

  @Override
  public ShellStatus executeCommand(Environment env, String arguments) {
    var args = parser.parse(arguments);
    if (args.length > 2) {
      env.writeln("Expected <property> [, <new_value>] arguments");
      return ShellStatus.CONTINUE;
    }
    Consumer<String[]> strategy = args.length == 2 ?
      var -> env.setEnvironmentVar(var[0], var[1]) :
      var -> env.writeln(env.getEnvironmentVar(var[0]));
    strategy.accept(args);
    return ShellStatus.CONTINUE;
  }
}
