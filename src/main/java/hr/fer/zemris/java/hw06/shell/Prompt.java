package hr.fer.zemris.java.hw06.shell;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

/**
 * Shell command that loops until exit condition is met.
 * Essentially, acts as a subshell that can be killed by
 * exit command if it was registered.
 */
public class Prompt implements ShellCommand {

  private SortedMap<String, ShellCommand> commands = new TreeMap<>();
  private CommandParser parser;
  private Character multilineChar;
  private String prompt;
  private String promptContiation;

  private InputStream in = System.in;
  private OutputStream out = System.out;

  /**
   * @param in input stream for which shell should read from
   */
  public void setInputStream(InputStream in) {
    this.in = in;
  }

  /**
   * @param exitAction adds exit command for prompt to exit from loop when typed
   */
  public final void setExitCommand(String exitAction) {
    // simply adds command that returns TERMINATE signal for terminal.
    commands.put(exitAction, (env, arguments) -> ShellStatus.TERMINATE);
  }

  /**
   * Simply spins a loop for subshell.
   * This allows us to nest shells for subprograms.
   *
   * @param env ignored
   * @param arguments ignored
   * @return always {@link ShellStatus#CONTINUE}
   */
  @Override
  public ShellStatus executeCommand(Environment env, String arguments) {
    var args = parser.parse(arguments);
    env.write(env.getPromptSymbol());
    var sc = new Scanner(in);
    while (sc.hasNextLine()) {
      var actionLiteral = sc.nextLine().strip().split(" ");
      if (actionLiteral.length == 0)
        continue; // user didn't enter anything
      if (!commands.containsKey(actionLiteral[0])) // command not registered
        System.out.println("Command doesn't exist " + actionLiteral[0]);
      else {
        var status = commands.get(actionLiteral[0]).executeCommand(env, arguments);
        if (status == ShellStatus.TERMINATE)
          break; // command ordered shell termination
      }
    }
    return ShellStatus.CONTINUE; // in case, this is a subshell - parent shell continues normal execution
  }
}
