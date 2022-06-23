package hr.fer.oprpp1.custom.collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DictionaryTest {

  private Dictionary<String, Integer> dict;

  @BeforeEach
  public void setup() {
    dict = new Dictionary<>();
  }


  @Test
  public void testPut() {
    dict.put("a", 1);
    assertEquals(1, dict.size());
    assertEquals(1, dict.get("a"));
  }

  @Test
  public void testRemove() {
    dict.put("a", 1);
    assertEquals(1, dict.size());
    dict.remove("a");
    assertEquals(0, dict.size());
  }

  @Test
  public void testReplace() {
    dict.put("b", 4);
    dict.put("c", 354);
    var res = dict.put("a", 1);
    assertNull(res);
    var one = dict.put("a", 2);
    assertEquals(1, one);
  }

  @Test
  public void testClear() {
    dict.put("a", 1);
    dict.put("b", 2);
    dict.put("c", 3);
    assertEquals(3, dict.size());
    dict.clear();
    assertEquals(0, dict.size());
  }

  /*---------- GENERICS TESTS ----------*/

  class A {}
  class B extends A {}

  @Test
  public void testGenerice() {
    var d = new Dictionary<Integer, A>();
    d.put(0, new A());
    d.put(1, new B());
    d.put(2, new B());
    assertEquals(3, d.size());
  }

}