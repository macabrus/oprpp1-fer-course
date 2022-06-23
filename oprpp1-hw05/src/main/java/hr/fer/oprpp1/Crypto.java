package hr.fer.oprpp1;

import hr.fer.zemris.java.hw06.shell.CommandParserFactory;
import hr.fer.zemris.java.hw06.shell.Prompt;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import java.security.NoSuchAlgorithmException;

/**
 * Interactive Crypto shell
 */
public class Crypto {

  /**
   * This shell can be tested by executing the following commands:
   *  > encrypt testfile.txt testfile.crypt
   *  > e52217e3ee213ef1ffdee3a192e2ac7e
   *  > 000102030405060708090a0b0c0d0e0f
   *  > decrypt testfile.crypt testfile.orig.txt
   *  > checksha testfile.orig.txt
   *  > 62ee21fdb4eb94148e0792c134324ba06c1d105c9ec43cb7165cca77a6694ea7
   *  > checksha testfile.txt
   *  > 62ee21fdb4eb94148e0792c134324ba06c1d105c9ec43cb7165cca77a6694ea7
   *  > exit
   * It should return all positive results because encrypted which is then decrypted
   * should have the same digest as original one.
   * Of course, password and initialization vector can be changed.
   *
   * @param args ignored
   * @throws NoSuchAlgorithmException if this weird port of JDK doesn't include standard cryptographic algorithms (?)
   * @throws NoSuchPaddingException if padding scheme isn't supported
   */
  // napomena: promijenio sam ovo u shell nakon što sam napisao
  public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchPaddingException {
    // default parser definition for prompt commands
    var promptParser = CommandParserFactory.getInstance().getDefaultPromptParser();
    Prompt prompt = new Prompt("Crypto > ", promptParser); // interactive mode

    var cmdParser = CommandParserFactory.getInstance().getDefault();
    prompt.registerCommand("checksha", new CheckSHACommand("SHA-256", cmdParser));
    prompt.registerCommand("encrypt", new CipherCommand("AES/CBC/PKCS5Padding", Cipher.ENCRYPT_MODE, cmdParser));
    prompt.registerCommand("decrypt", new CipherCommand("AES/CBC/PKCS5Padding", Cipher.DECRYPT_MODE, cmdParser));
    prompt.registerExitCommand("exit");
    prompt.executeCommand(prompt, null);
  }
}
