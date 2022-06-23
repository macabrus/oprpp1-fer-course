package hr.fer.oprpp1.custom.scripting.lexer;


import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static hr.fer.oprpp1.custom.scripting.lexer.TokenType.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SmartScriptLexerTest {

  private String read(String exampleName) {
    var realPath = getClass().getResource("/smart_script").getPath();
    try {
      return Files.readString(Path.of(realPath, exampleName));
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  @Test
  public void testNullThrows() {
    assertThrows(SmartScriptLexerException.class, () -> new SmartScriptLexer(null));
  }

  @Test
  public void testExceptions() {
    var lexer = new SmartScriptLexer(read("wrong.txt"));
    assertEquals(TAG_START, lexer.nextToken().getType());
    lexer.setState(LexerState.TAG);
    assertEquals(VAR, lexer.nextToken().getType());
    assertThrows(SmartScriptLexerException.class, lexer::nextToken);
  }

  @Test
  public void testStickySequence() {
    var lexer = new SmartScriptLexer(read("sticky.txt"));
    assertEquals(TAG_START, lexer.nextToken().getType());
    lexer.setState(LexerState.TAG);
    assertEquals(VAR, lexer.nextToken().getType());
    assertEquals(VAR, lexer.nextToken().getType());
    assertEquals(INT, lexer.nextToken().getType());
    assertEquals(VAR, lexer.nextToken().getType());
    assertEquals(STRING, lexer.nextToken().getType());
    assertEquals(TAG_CLOSE, lexer.nextToken().getType());
    lexer.setState(LexerState.TEXT);
    assertEquals("\n", lexer.nextToken().getValue());
    assertEquals(TAG_START, lexer.nextToken().getType());
    lexer.setState(LexerState.TAG);
    assertEquals("END", lexer.nextToken().getValue());
    assertEquals(TAG_CLOSE, lexer.nextToken().getType());
    assertEquals(EOF, lexer.nextToken().getType());
  }

}