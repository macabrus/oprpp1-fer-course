package hr.fer.oprpp1.db;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ComparisonOperatorsTest {

  @Test
  public void testLess() {
    assertTrue(ComparisonOperators.LESS.satisfied("A", "B"));
    assertFalse(ComparisonOperators.LESS.satisfied("B", "A"));
  }

  @Test
  public void testGreater() {
    assertFalse(ComparisonOperators.GREATER.satisfied("A", "B"));
    assertTrue(ComparisonOperators.GREATER.satisfied("B", "A"));
  }

  @Test
  public void testLessOrEquals() {
    assertTrue(ComparisonOperators.LESS_OR_EQUALS.satisfied("A", "B"));
    assertTrue(ComparisonOperators.LESS_OR_EQUALS.satisfied("A", "A"));
    assertFalse(ComparisonOperators.LESS_OR_EQUALS.satisfied("B", "A"));
  }

  @Test
  public void testGreaterOrEquals() {
    assertFalse(ComparisonOperators.GREATER_OR_EQUALS.satisfied("A", "B"));
    assertTrue(ComparisonOperators.GREATER_OR_EQUALS.satisfied("A", "A"));
    assertTrue(ComparisonOperators.GREATER_OR_EQUALS.satisfied("B", "A"));
  }

  @Test
  public void testEquals() {
    assertTrue(ComparisonOperators.EQUALS.satisfied("ASDF", "ASDF"));
    assertFalse(ComparisonOperators.EQUALS.satisfied("ASDF", "ASDFQ"));
  }

  @Test
  public void testNotEquals() {
    assertFalse(ComparisonOperators.NOT_EQUALS.satisfied("ASDF", "ASDF"));
    assertTrue(ComparisonOperators.NOT_EQUALS.satisfied("ASDF", "ASDFQ"));
  }

  /**
   * As far as my understanding goes, this is not a Kleene operator.
   * It doesn't consider preceding character but simply matches anything.
   * Otherwise * in the beginning of string should be considered an error,
   * but it is legal as specified in homework task.
   *
   * Equivalent regex would be something like '.*'
   */
  @Test
  public void testLike() {
    assertTrue(ComparisonOperators.LIKE.satisfied("nfngjda", "*a"));
    assertTrue(ComparisonOperators.LIKE.satisfied("mrfkjnmgkj", "mrf*kj"));
    assertFalse(ComparisonOperators.LIKE.satisfied("AAA", "AA*AA"));
    assertFalse(ComparisonOperators.LIKE.satisfied("AAA", "AA*AA"));
  }


}