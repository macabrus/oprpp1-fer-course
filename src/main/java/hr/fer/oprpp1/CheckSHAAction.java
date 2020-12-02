package hr.fer.oprpp1;

import hr.fer.oprpp1.custom.Action;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class CheckSHAAction implements Action {

  private MessageDigest md;
  private String spec;

  public CheckSHAAction(String spec) throws NoSuchAlgorithmException {
    this.spec = spec;
    md = MessageDigest.getInstance(spec);
  }

  @Override
  public void perform(String[] args) {
    if (args.length != 1)
      throw new IllegalArgumentException("Expected 1 arguments: <filename>");

    var expected = promptExpectedChecksum(args[0]);

    try (var in = new FileInputStream(args[0])) {
      var buff = new byte[4096];
      int read;
      while ((read = in.read(buff)) != -1)
        md.update(buff, 0, read);
      System.out.println(
        "Digesting completed. Digest of %s %s expected digest.".formatted(
          args[0],
          expected.equals(Util.buffToHex(md.digest())) ? "matches" : "doesn't match"
        )
      );
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private String promptExpectedChecksum(String file) {
    System.out.println("Please provide expected sha-256 digest for %s:".formatted(file));
    System.out.print(" > ");
    return new Scanner(System.in).next();
  }
}
