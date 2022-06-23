package hr.fer.zemris.java.hw06.shell;


import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

public class TreeCommand implements ShellCommand {

  private CommandParser parser;

  public TreeCommand(CommandParser parser) {
    this.parser = parser;
  }

  @Override
  public ShellStatus executeCommand(Environment env, String arguments) {
    var args = parser.parse(arguments);
    if (args.length != 1) {
      env.writeln("Expected single <directory_path> argument");
      return ShellStatus.CONTINUE;
    }
    try {
      Files.walkFileTree(Path.of(args[0]), new SimpleFileVisitor<>() {

        private int indent = 0;

        private void writeName(Path p) {
          env.writeln("  ".repeat(indent) + p.getFileName());
        }

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
          writeName(file);
          return super.visitFile(file, attrs);
        }

        @Override
        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
          writeName(dir);
          indent ++;
          return super.preVisitDirectory(dir, attrs);
        }

        @Override
        public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
          indent --;
          return super.postVisitDirectory(dir, exc);
        }
      });
    } catch (IOException e) {
      env.writeln("An error occured while listing directory tree.");
    }
    return ShellStatus.CONTINUE;
  }
}
