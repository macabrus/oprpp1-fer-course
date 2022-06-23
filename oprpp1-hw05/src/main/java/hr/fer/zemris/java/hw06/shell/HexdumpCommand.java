package hr.fer.zemris.java.hw06.shell;

import hr.fer.oprpp1.Util;

import java.io.FileInputStream;
import java.io.IOException;

public class HexdumpCommand implements ShellCommand {

  private CommandParser parser;

  public HexdumpCommand(CommandParser parser) {
    this.parser = parser;
  }

  @Override
  public ShellStatus executeCommand(Environment env, String arguments) {
    var args = parser.parse(arguments);
    if (args.length != 1) {
      env.writeln("Expected <file_path> as an argument.");
    }
    try (var in = new FileInputStream(args[0])) {
      // TODO
      byte[] b = new byte[4096];
      int readCount; // total buffer size
      int currentChar = 0; // current char position in buffer to print
      int currentBlockLen = 0; // current block length, at 16, it resets
      while ( (readCount = in.read(b)) != -1) {
        currentChar = 0;
        while (readCount != currentChar) {
          if (currentChar % 16 == 0)
            env.write("%08X".formatted(currentChar) + ":");
          for (int i = 0; i < 16 && currentChar < readCount; i++) {
            if (i == 8)
              env.write("|");
            else
              env.write(" ");
            env.write(Util.byteToHex(b[currentChar++]));
            currentBlockLen++;
          }
          if (currentBlockLen % 16 == 0 ) {
            env.write(" | ");
            for (int i = currentChar - 16; i < currentChar; i++)
              env.write(String.valueOf(b[i] < 32 || b[i] > 127 ? '.' : (char) b[i]));
            env.writeln("");
            currentBlockLen = 0;
          }
        }
      }
      // last pad if block was not full
      if (currentBlockLen % 16 != 0) {
        env.write("   ".repeat(16 - currentBlockLen) + " | ");
        for (int i = currentChar - currentBlockLen; i < currentChar; i++)
          env.write(String.valueOf(b[i] < 32 || b[i] > 127 ? '.' : (char) b[i]));
        env.writeln("");
      }
    } catch (IOException e) {
      env.writeln("Error while reading file.");
    }
    return ShellStatus.CONTINUE;
  }
}
