package hr.fer.oprpp1;

import hr.fer.zemris.java.hw06.shell.CommandParser;
import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class CheckSHACommand implements ShellCommand {

  private MessageDigest md;
  private String spec;
  private CommandParser parser;

  public CheckSHACommand(String spec, CommandParser parser) throws NoSuchAlgorithmException {
    this.spec = spec;
    this.md = MessageDigest.getInstance(spec);
    this.parser = parser;
  }

  private String promptExpectedChecksum(Environment env, String file) {
    env.writeln("Please provide expected sha-256 digest for %s:".formatted(file));
    env.write(" > ");
    return env.readLine();
  }

  @Override
  public ShellStatus executeCommand(Environment env, String arguments) {
    var args = parser.parse(arguments);
    if (args.length != 1)
      env.writeln("Expected 1 arguments: <filename>");

    var expected = promptExpectedChecksum(env, args[0]);

    try (var in = new FileInputStream(args[0])) {
      var buff = new byte[4096];
      int read;
      while ((read = in.read(buff)) != -1)
        md.update(buff, 0, read);
      env.writeln(
        "Digesting completed. Digest of %s %s expected digest.".formatted(
          args[0],
          expected.equals(Util.buffToHex(md.digest())) ? "matches" : "doesn't match"
        )
      );
    } catch (IOException e) {
      //e.printStackTrace();
      env.writeln("File doesn't exist or your user doesn't have permission to read this file.");

    }
    return ShellStatus.CONTINUE;
  }

  @Override
  public String getCommandName() {
    return "CheckSHA";
  }

  @Override
  public List<String> getCommandDescription() {
    return List.of("Computes SHA sum of given file.");
  }

}
