package hr.fer.oprpp1.demo;

import hr.fer.oprpp1.custom.collections.Tester;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class EvenIntegerTesterTest {

  private Tester tester = new EvenIntegerTester();

  @Test
  public void testerWorksCorrectly() {
    assertFalse(tester.test("Ivo"));
    assertTrue(tester.test(22));
    assertFalse(tester.test(3));
  }
}