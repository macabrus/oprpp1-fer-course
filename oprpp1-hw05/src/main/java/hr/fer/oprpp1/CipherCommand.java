package hr.fer.oprpp1;

import hr.fer.zemris.java.hw06.shell.CommandParser;
import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;

/**
 * AES cipher shell command. It can be registered
 * to some implementation of Prompt shell and used
 * interactively as a subshell. However, it only
 * has two prompts so I didn't see a point in implementing
 * special subshell for this command.
 */
public class CipherCommand implements ShellCommand {

  private String spec;
  private final int mode;
  private Cipher cipher;
  private CommandParser parser;

  public CipherCommand(String spec, int mode, CommandParser parser) throws NoSuchPaddingException, NoSuchAlgorithmException {
    this.spec = spec;
    this.mode = mode;
    this.cipher = Cipher.getInstance(spec);
    this.parser = parser;
  }


  private String askPassword(Environment env) {
    env.writeln("Please provide password as hex-encoded text (16 bytes, i.e. 32 hex-digits):");
    env.write(" > ");
    return env.readLine();
  }

  private String askInitVector(Environment env) {
    env.writeln("Please provide initialization vector as hex-encoded text (32 hex-digits):");
    env.write(" > ");
    return env.readLine();
  }

  @Override
  public ShellStatus executeCommand(Environment env, String arguments) {
    var args = parser.parse(arguments);
    if (args.length != 2)
      env.writeln("Expected 2 arguments: <source_filename> <encrypted_filename>");

    var pass = Util.hexToBuff(askPassword(env));
    var iv = Util.hexToBuff(askInitVector(env));

    SecretKeySpec keySpec = new SecretKeySpec(pass, "AES");
    AlgorithmParameterSpec paramSpec = new IvParameterSpec(iv);

    try {
      cipher.init(mode, keySpec, paramSpec);
    } catch (InvalidKeyException | InvalidAlgorithmParameterException e) {
      //e.printStackTrace();

    }

    try(var in = new FileInputStream(args[0]);
        var out = new FileOutputStream(args[1])) {
      var buff = new byte[4096];
      int read;
      while ((read = in.read(buff)) != -1) {
        var encryptedBuff = cipher.update(buff, 0, read);
        if (encryptedBuff != null)
          out.write(encryptedBuff);
      }
      var last = cipher.doFinal();
      out.write(last);
      out.flush();
    } catch (IOException | BadPaddingException | IllegalBlockSizeException e) {
      //e.printStackTrace();
      env.writeln("File %s could not be encrypted because it doesn't exist.");
    }
    return ShellStatus.CONTINUE;
  }

}
