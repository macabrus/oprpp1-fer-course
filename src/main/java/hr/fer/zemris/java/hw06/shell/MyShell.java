package hr.fer.zemris.java.hw06.shell;

import java.nio.charset.Charset;

public class MyShell {

  public static void main(String[] args) {
    // default parser definition for prompt commands
    CommandParser promptParser = line -> {
      line = line.stripLeading();
      var firstSpace = line.indexOf(" ");
      if (firstSpace == -1)
        return new String [] {line, ""};
      return new String[] {line.substring(0, firstSpace), line.substring(firstSpace)};
    };

    // default prompt for shell
    var prompt = new Prompt(" MyShell (v1.0) > ", promptParser);

    // default parser for every command (can be redefined anytime)
    CommandParser argParser = CommandParserFactory.getInstance().getDefault();

    // registering commands
    prompt.registerExitCommand("exit");
    prompt.registerCommand("ls", new LsCommand(argParser));
    prompt.registerCommand("symbol", new SymbolCommand(argParser));
    prompt.registerCommand("charsets", (env, arguments) -> {
      Charset.availableCharsets().forEach((k, v) -> env.writeln(k));
      return ShellStatus.CONTINUE;
    });
    prompt.registerCommand("cat", new CatCommand(argParser));
    prompt.registerCommand("tree", new TreeCommand(argParser));
    prompt.registerCommand("copy", new CopyCommand(argParser));
    prompt.registerCommand("mkdirs", new MkdirsCommand(argParser));
    prompt.registerCommand("hexdump", new HexdumpCommand(argParser));
    prompt.registerCommand("help", new HelpCommand(argParser));

    prompt.executeCommand(prompt, "");

  }
}
