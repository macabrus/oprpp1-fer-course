package hr.fer.oprpp1;

import hr.fer.zemris.java.hw06.shell.CommandParserFactory;
import hr.fer.zemris.java.hw06.shell.Prompt;
import hr.fer.zemris.java.hw06.shell.ShellStatus;
import org.junit.jupiter.api.*;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Since unit testing these components depends on environment,
 * it is more of a integration testing which requires some
 * broader context like underlying file system to read from and
 * write to.
 *
 * I use {@link ByteArrayOutputStream} to NOT write garbage to console
 * while unit testing because by default, shell uses sys.out as output
 * stream to write to.
 *
 * These unit tests simulate
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CryptoShellTest {

  Prompt prompt;

  @BeforeEach
  public void setupEnv() throws NoSuchAlgorithmException, NoSuchPaddingException {
    var promptParser = CommandParserFactory.getInstance().getDefaultPromptParser();
    var cmdParser = CommandParserFactory.getInstance().getDefault();
    prompt = new Prompt("Crypto > ", promptParser); // interactive mode
    prompt.registerCommand("checksha", new CheckSHACommand("SHA-256", cmdParser));
    prompt.registerCommand("encrypt", new CipherCommand("AES/CBC/PKCS5Padding", Cipher.ENCRYPT_MODE, cmdParser));
    prompt.registerCommand("decrypt", new CipherCommand("AES/CBC/PKCS5Padding", Cipher.DECRYPT_MODE, cmdParser));
    prompt.registerExitCommand("exit");
  }

  @Test
  @Order(0)
  public void testHashingWorks() {
    var script = "checksha testfile.txt\n" +
                 "62ee21fdb4eb94148e0792c134324ba06c1d105c9ec43cb7165cca77a6694ea7\n" +
                 "exit\n";
    prompt.setIn(new BufferedInputStream(new ByteArrayInputStream(script.getBytes())));
    var os = new ByteArrayOutputStream();
    prompt.setOut(os);
    prompt.executeCommand(prompt, null);
    var output = os.toString();
    assertTrue(os.toString().contains("matches"));
  }

  @Test
  @Order(1)
  public void testEncryptionWorks() {
    var script = "encrypt testfile.txt testfile.crypt\n" +
                 "e52217e3ee213ef1ffdee3a192e2ac7e\n" +
                 "000102030405060708090a0b0c0d0e0f\n" +
                 "exit\n";
    prompt.setIn(new BufferedInputStream(new ByteArrayInputStream(script.getBytes())));
    var os = new ByteArrayOutputStream();
//    prompt.setOut(os);
    var status = prompt.executeCommand(prompt, null);
    assertEquals(ShellStatus.CONTINUE, status);
  }

  @Test
  @Order(2)
  public void testDecryptionWorks() throws IOException {
    var script = "decrypt testfile.crypt testfile.orig.txt\n" +
                 "e52217e3ee213ef1ffdee3a192e2ac7e\n" +
                 "000102030405060708090a0b0c0d0e0f\n" +
                 "exit\n";
    prompt.setIn(new BufferedInputStream(new ByteArrayInputStream(script.getBytes())));
    var os = new ByteArrayOutputStream();
//    prompt.setOut(os);
    var status = prompt.executeCommand(prompt, null);
    assertEquals(ShellStatus.CONTINUE, status);
    assertEquals(Files.readString(Path.of("testfile.txt")), Files.readString(Path.of("testfile.orig.txt")));
  }

  @BeforeAll
  @AfterAll
  public static void cleanup() throws IOException {
    Files.deleteIfExists(Path.of("testfile.crypt"));
    Files.deleteIfExists(Path.of("testfile.orig.txt"));
  }

}