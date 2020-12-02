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
import java.util.Scanner;

public class CipherAction implements ShellCommand {

  private String spec;
  private final int mode;
  private Cipher cipher;
  private CommandParser parser;

  public CipherAction(String spec, int mode) throws NoSuchPaddingException, NoSuchAlgorithmException {
    this.spec = spec;
    this.mode = mode;
    cipher = Cipher.getInstance(spec);
  }


  private String askPassword() {
    System.out.println("Please provide password as hex-encoded text (16 bytes, i.e. 32 hex-digits):");
    System.out.print(" > ");
    return new Scanner(System.in).next();
  }

  private String askInitVector() {
    System.out.println("Please provide initialization vector as hex-encoded text (32 hex-digits):");
    System.out.print(" > ");
    return new Scanner(System.in).next();
  }

  @Override
  public ShellStatus executeCommand(Environment env, String arguments) {
    var args = parser.parse(arguments);
    if (args.length != 2)
      throw new IllegalArgumentException("Expected 2 arguments: <source_filename> <encrypted_filename>");

    var pass = Util.hexToBuff(askPassword());
    var iv = Util.hexToBuff(askInitVector());

    SecretKeySpec keySpec = new SecretKeySpec(pass, "AES");
    AlgorithmParameterSpec paramSpec = new IvParameterSpec(iv);

    try {
      cipher.init(mode, keySpec, paramSpec);
    } catch (InvalidKeyException | InvalidAlgorithmParameterException e) {
      e.printStackTrace();
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
      e.printStackTrace();
    }
    return ShellStatus.CONTINUE;
  }

  @Override
  public void setCommandParser(CommandParser parser) {
    this.parser = parser;
  }
}
