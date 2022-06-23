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
public class Prompt implements ShellCommand, Environment {

  /**
   * Contains environment variables that can be changed at runtime and
   * are used as shell is working. Shell passes whole environment to
   * child processes (sub-commands) while it is itself a entry command.
   *
   * This map could be used in future to hold CWD variable to keep current
   * path context which could be changed by registering UNIX-style command "cd"
   * for changing directories.
   */
  private Map<String, String> ENVIRONMENT_VARS = new HashMap<>() {{
    put("PROMPT", " > ");
    put("MULTILINE", " | ");
    put("MORELINES", "\\");
  }};

  private SortedMap<String, ShellCommand> commands = new TreeMap<>();
  private CommandParser parser;

  // It defaults to System out and System in
  private InputStream in = System.in;
  private OutputStream out = System.out;

  public Prompt(String prompt, CommandParser parser) {
    this(parser);
    ENVIRONMENT_VARS.put("PROMPT", prompt);
  }

  public Prompt(CommandParser parser) {
    this.parser = parser;
  }

  public void setOut(OutputStream out) {
    this.out = out;
  }

  public void setIn(InputStream in) {
    this.in = in;
  }

  /**
   * @param exitAction adds exit command for prompt to exit from loop when typed
   */
  public final void registerExitCommand(String exitAction) {
    // simply adds command that returns TERMINATE signal for terminal.
    registerCommand(exitAction, (env, arguments) -> ShellStatus.TERMINATE);
  }

  public final void registerCommand(String name, ShellCommand cmd) {
    commands.put(name, cmd);
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
    while (true) {
      env.write(env.getPromptSymbol());
      var line = readLine();
      if (line == null)
        return ShellStatus.CONTINUE;
      var cmd = parser.parse(line);
      if (cmd[0].equals(""))
        continue;
      if (!commands.containsKey(cmd[0])) // command not registered
        env.writeln("Command '%s' doesn't exist ".formatted(cmd[0]));
      else {
        var status = commands.get(cmd[0]).executeCommand(env, cmd[1]);
        if (status == ShellStatus.TERMINATE)
          break; // command ordered shell termination
      }
    }
    return ShellStatus.CONTINUE; // in case this is a subshell -> parent shell continues normal execution
  }

  private Scanner sc;
  /**
   * @return line to be consumed
   * @throws ShellIOException if there was a problem while reading a stream
   */
  @Override
  public String readLine() throws ShellIOException {
    if (sc == null)
      sc = new Scanner(in);
    var sb = new StringBuilder();
    while (sc.hasNextLine()) {
      var part = sc.nextLine();
      if (part.matches(".*\\" + ENVIRONMENT_VARS.get("MORELINES") + "\\s*$")) {
        sb.append(part.replaceFirst("\\" + ENVIRONMENT_VARS.get("MORELINES") + "\\s*$", ""));
        write(ENVIRONMENT_VARS.get("MULTILINE"));
      }
      else {
        sb.append(part);
        return sb.toString();
      }
    }
    return null;
  }

  @Override
  public void write(String text) throws ShellIOException {
    try {
      out.write(text.getBytes());
    } catch (IOException e) {
      throw new ShellIOException("Error occured while writing to output stream.");
    }
  }

  @Override
  public void writeln(String text) throws ShellIOException {
    // TODO: wrap in PrintWriter which can automatically insert system default line endings
    // depending on platform
    try {
      out.write((text + '\n').getBytes());
    } catch (IOException e) {
      throw new ShellIOException("Error occured while writing to output stream.");
    }
  }

  @Override
  public SortedMap<String, ShellCommand> commands() {
    return Collections.unmodifiableSortedMap(commands);
  }

  @Override
  public String getMultilineSymbol() {
    return ENVIRONMENT_VARS.get("MULTILINE");
  }

  @Override
  public void setMultilineSymbol(String symbol) {
    ENVIRONMENT_VARS.put("MULTILINE", symbol);
  }

  @Override
  public String getPromptSymbol() {
    return ENVIRONMENT_VARS.get("PROMPT");
  }

  @Override
  public void setPromptSymbol(String symbol) {
    ENVIRONMENT_VARS.put("PROMPT", symbol);
  }

  @Override
  public Character getMorelinesSymbol() {
    return ENVIRONMENT_VARS.get("MORELINES").charAt(0);
  }

  @Override
  public void setMorelinesSymbol(Character symbol) {
    ENVIRONMENT_VARS.put("MORELINES", String.valueOf(symbol));
  }

  @Override
  public void setEnvironmentVar(String var, String value) {
    ENVIRONMENT_VARS.put(var, value);
  }

  @Override
  public String getEnvironmentVar(String var) {
    return ENVIRONMENT_VARS.get(var);
  }
}
