package hr.fer.oprpp1;

import hr.fer.zemris.java.hw06.shell.CommandParser;
import hr.fer.zemris.java.hw06.shell.CommandParserFactory;
import hr.fer.zemris.java.hw06.shell.Prompt;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Since unit testing these components depends on environment,
 * it is more of a integration testing which requires some
 * broader context like underlying file system to read from and
 * write to.
 *
 * These unit tests simulate
 */
class CryptoShellTest {

  Prompt prompt;

  @BeforeEach
  public void setupEnv() throws NoSuchAlgorithmException {
    var promptParser = CommandParserFactory.getInstance().getDefaultPromptParser();
    prompt = new Prompt("Crypto > ", promptParser); // interactive mode
    var sha = new CheckSHACommand("SHA-256", CommandParserFactory.getInstance().getDefault());
    prompt.registerCommand("checksha", sha);
    prompt.registerExitCommand("exit");
  }

  // TODO: make shell testable by using dummy input stream
  // It currently doesn't work since it uses Scanner under
  // the hood which 'eats' the stream by buffering it.
  // So the rest of the lines are then ignored...
  // It will be fixed by keeping the prompt scanner alive.
  // throughout its lifetime.
  @Test
  public void testHashingWorks() {
//    var script = "checksha testfile.txt\n" +
//                 "62ee21fdb4eb94148e0792c134324ba06c1d105c9ec43cb7165cca77a6694ea7\n" +
//                 "exit\n";
//    prompt.setIn(new BufferedInputStream(new ByteArrayInputStream(script.getBytes())));
//    var os = new ByteArrayOutputStream();
//    //prompt.setOut(os);
//    prompt.executeCommand(prompt, null);
//    var output = os.toString();
//    assertEquals("", output);
  }

  @Test
  public void testEncryptionWorks() {

  }

  @Test
  public void testDecryptionWorks() {

  }

  @AfterAll
  public static void cleanup() {
    // removing all leftover files
  }

}