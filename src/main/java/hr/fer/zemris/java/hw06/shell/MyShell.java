package hr.fer.zemris.java.hw06.shell;

public class MyShell {

  public static void main(String[] args) {
    var prompt = new Prompt(" MyShell (v1.0) > ");
    pro
    shell.in = System.in;
    shell.out = System.out;
    shell.setMultilineChar('\\');
    shell.setPromptSymbol(" MyShell (1.0) > ");
    shell.setExitCommand("exit");
    shell.commands().put("ls", new LsCommand());
//    SortedMap<String, ShellCommand> commands = new TreeMap<>();
//    commands.put("exit", new ExitCommand());
//    commands.put("ls", new LsCommand());
//    commands.put("charsets", new CharsetsCommand());
//    commands.put("cat", new CatCommand());
//    commands.put("tree", new TreeCommand());
//    commands.put("copy", new CopyCommand());
//    commands.put("mkdir", new MkdirCommand());
//    commands.put("hexdump", new HexdumpCommand());

  }
}
