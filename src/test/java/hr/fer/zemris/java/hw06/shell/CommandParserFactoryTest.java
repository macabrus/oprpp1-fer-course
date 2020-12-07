package hr.fer.zemris.java.hw06.shell;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class CommandParserFactoryTest {

  private CommandParserFactory factory;

  @BeforeEach
  public void setup() {
    factory = CommandParserFactory.getInstance();
  }

  @Test
  public void testDefaultParser() {
    var args = factory.getDefault().parse("this is an \"fdg \\ /d/dfg df g\" command");
    assertArrayEquals(new String[] {"this", "is", "an", "fdg \\ /d/dfg df g", "command"}, args);
  }

}