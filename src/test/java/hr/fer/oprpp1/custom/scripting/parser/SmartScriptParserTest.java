package hr.fer.oprpp1.custom.scripting.parser;

import hr.fer.oprpp1.custom.scripting.lexer.SmartScriptLexerException;
import hr.fer.oprpp1.custom.scripting.nodes.EchoNode;
import hr.fer.oprpp1.custom.scripting.nodes.ForLoopNode;
import hr.fer.oprpp1.custom.scripting.nodes.TextNode;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class SmartScriptParserTest {

  private String read(String exampleName) {
    var examplesPath = SmartScriptParserTest.class.getResource("/document_examples").getPath();
    var extrasPath = SmartScriptParserTest.class.getResource("/extra").getPath();
    try {
      if (!Files.exists(Path.of(examplesPath, exampleName)))
        return Files.readString(Path.of(extrasPath, exampleName));
      return Files.readString(Path.of(examplesPath, exampleName));
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  private String readExample(int n) {
    try(InputStream is = this.getClass().getClassLoader().getResourceAsStream("extra/primjer"+n+".txt")) {
      if(is==null) throw new RuntimeException("Datoteka extra/primjer"+n+".txt je nedostupna.");
      byte[] data = this.getClass().getClassLoader().getResourceAsStream("extra/primjer" + n + ".txt").readAllBytes();
      String text = new String(data, StandardCharsets.UTF_8);
      return text;
    } catch(IOException ex) {
      throw new RuntimeException("Greška pri čitanju datoteke.", ex);
    }
  }

  @Test
  public void testNullThrows() {
    assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser(null));
  }

  @Test
  public void testEmptyDocumentWorks() {
    assertDoesNotThrow(() -> new SmartScriptParser(read("empty.txt")));
    var parser = new SmartScriptParser(read("empty.txt"));
    assertEquals(0, parser.getDocumentNode().numberOfChildren());
  }

  @Test
  public void testSimpleText() {
    var parser = new SmartScriptParser(read("text.txt"));
    // Expecting only single child
    assertEquals(1, parser.getDocumentNode().numberOfChildren());
  }

  @Test
  public void testDocumentFromHomework() {
    assertDoesNotThrow(() -> new SmartScriptParser(read("document1.txt")));
    var parser = new SmartScriptParser(read("document1.txt"));
    var node = (EchoNode) parser.getDocumentNode().getChild(3).getChild(1);
    assertEquals(1, node.getElements().length);
    assertEquals("i", node.getElements()[0].asText());
  }

  @Test
  public void testMoreComplexDocument() {
    assertDoesNotThrow(() -> new SmartScriptParser(read("complicated.txt")));
    var parser = new SmartScriptParser(read("complicated.txt"));
    var node = (ForLoopNode) parser.getDocumentNode().getChild(0);
    assertEquals("a", node.getVariable().asText());
    assertEquals("0", node.getStartExpression().asText());
    assertEquals("10", node.getEndExpression().asText());
    assertNull(node.getStepExpression());
  }

  @Test
  public void testReparsingYieldsSameResult() {
    var parser = new SmartScriptParser(read("complicated.txt"));
    var serialized = parser.getDocumentNode().toString();
    var parser2 = new SmartScriptParser(serialized);
    var serialized2 = parser2.getDocumentNode().toString();
    assertEquals(serialized, serialized2);
  }

  /*---------- HOMEWORK EXAMPLES ----------*/

  @Test
  public void testPrimjer1 () {
    var parser = new SmartScriptParser(readExample(1));
    var doc = parser.getDocumentNode();
    assertEquals(1, doc.numberOfChildren());
    assertEquals(TextNode.class, doc.getChild(0).getClass());
  }

  @Test
  public void testPrimjer2() {
    var parser = new SmartScriptParser(readExample(2));
    var doc = parser.getDocumentNode();
    assertEquals(1, doc.numberOfChildren());
  }

  @Test
  public void testPrimjer3() {
    var parser = new SmartScriptParser(readExample(3));
    var doc = parser.getDocumentNode();
    assertEquals(1, doc.numberOfChildren());
  }

  @Test
  public void testPrimjer4() {
    assertThrows(SmartScriptLexerException.class, () -> {
      new SmartScriptParser(readExample(4));
    });
  }

  @Test
  public void testPrimjer5() {
    assertThrows(SmartScriptLexerException.class, () -> {
      new SmartScriptParser(readExample(5));
    });
  }

  @Test
  public void testPrimjer6() {
    assertDoesNotThrow(() -> {
      new SmartScriptParser(readExample(6));
    });
  }

  @Test
  public void testPrimjer7() {
    // Ovo mora bacati iznimku, iako piše u primjeru da je u redu. U pdf-u zadaće piše
    // Da je svaka escapeana sekvenca osim \{ i \\ ilegalna u textualnom modu izvođenja leksera
    // stoga, \n je ilegalan
    assertThrows(SmartScriptLexerException.class, () -> {
      new SmartScriptParser(readExample(7));
    });
  }

  @Test
  public void testPrimjer8() {
    // ovo se ne ruši jer je \{ legalna escape sekvenca i sve je jedan text node
    assertDoesNotThrow(() -> {
      new SmartScriptParser(readExample(8));
    });
  }

  @Test
  public void testPrimjer9() {
    // ovdje je ok očekivati da se ruši zbog \n
    assertThrows(SmartScriptLexerException.class, () -> {
      new SmartScriptParser(readExample(9));
    });
  }


}