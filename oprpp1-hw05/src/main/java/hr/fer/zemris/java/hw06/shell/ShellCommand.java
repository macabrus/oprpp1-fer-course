package hr.fer.zemris.java.hw06.shell;

import java.util.List;

/**
 * Universal interface for shell subprograms.
 * Shell passes env object to them so that they know
 * in which context to execute their operations.
 */
public interface ShellCommand {

  /**
   * @param env environment object with information about execution context
   * @param arguments parameters for command
   * @return whether calling shell should exit or continue working
   */
  ShellStatus executeCommand(Environment env, String arguments);

  /**
   * @return command name if specified
   */
  default String getCommandName() {
    return "Command name not defined.";
  }

  /**
   * @return man page of command if defined
   */
  default List<String> getCommandDescription() {
    return List.of("Command description not defined.");
  }

}
