package hr.fer.zemris.java.hw06.shell;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class LsCommand implements ShellCommand {

  private CommandParser parser;

  public LsCommand(CommandParser parser) {
    this.parser = parser;
  }

  @Override
  public ShellStatus executeCommand(Environment env, String arguments) {
    var args = parser.parse(arguments);
    if (args.length != 1) {
      env.writeln("'ls' expects a single <path> argument.");
      return ShellStatus.CONTINUE;
    }
    Path p = Paths.get(args[0]);
    try {
      for (String name : p.toFile().list()) {
        env.writeln(getFileAttrs(Paths.get(p.toAbsolutePath().toString(), name)));
      }
    } catch (NullPointerException npe) {
      env.writeln("Can't read directory " + args[0]);
    }
    return ShellStatus.CONTINUE;
  }

  private String getFileAttrs(Path path) {
    var sb = new StringBuilder();
    var f = path.toFile();
    sb.append(f.isDirectory() ? "d" : "-")
      .append(f.canRead() ? "r" : "-")
      .append(f.canWrite() ? "w" : "-")
      .append(f.canExecute() ? "x" : "-");
    BasicFileAttributeView faView = Files.getFileAttributeView(
      path, BasicFileAttributeView.class, LinkOption.NOFOLLOW_LINKS
    );
    try {
      var attributes = faView.readAttributes();
      sb.append("%19s".formatted(attributes.size()));
      FileTime fileTime = attributes.creationTime();
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      String formattedDateTime = sdf.format(new Date(fileTime.toMillis()));
      sb.append(" ").append(formattedDateTime);
    } catch (IOException e) {
      e.printStackTrace();
    }
    sb.append(" ").append(f.getName());
    return sb.toString();
  }

  @Override
  public String getCommandName() {
    return "ls";
  }

  @Override
  public List<String> getCommandDescription() {
    return List.of("This command is used to list a contents of a directory.");
  }
}
