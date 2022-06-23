package hr.fer.oprpp1.custom.hashtables;

import org.junit.jupiter.api.Test;

import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class SimpleHashtableTest {

  @Test
  public void testConstructors() {
    assertDoesNotThrow(() -> {
      new SimpleHashtable<Integer, Integer>(1);
      new SimpleHashtable<Integer, Integer>(2);
      new SimpleHashtable<Integer, Integer>(3);
      new SimpleHashtable<Integer, Integer>(100);
    });
    assertThrows(IllegalArgumentException.class, () -> {
      new SimpleHashtable<Integer, Integer>(0);
    });
  }

  // Creates simple hashtable with some values
  private SimpleHashtable<String, Integer> getHashTable() {
    var ht = new SimpleHashtable<String, Integer>();
    ht.put("a", 100);
    ht.put("aa", 200);
    ht.put("aaa", 400);
    ht.put("b", 3483);
    ht.put("bb", 2421);
    ht.put("bbb", 1905);
    ht.put("c", 6093);
    ht.put("cc", 1);
    ht.put("ccc", 452);
    ht.put("d", 100);
    ht.put("dd", 200);
    ht.put("ddd", 400);
    ht.put("e", 3483);
    ht.put("ee", 2421);
    ht.put("eee", 1905);
    ht.put("f", 6093);
    ht.put("ff", 45);
    ht.put("fff", 452);
    return ht;
  }

  @Test
  public void testPut() {
    var table = new SimpleHashtable<Integer, Integer>();
    table.put(1,2);
    table.put(3,4);
    table.put(5,6);
    table.put(7,8);
    table.toArray();
  }

  @Test
  public void testContainsKey() {
    var table = getHashTable();
    assertTrue(table.containsKey("a"));
    assertTrue(table.containsKey("b"));
    assertTrue(table.containsKey("c"));
    assertTrue(table.containsKey("d"));
    assertTrue(table.containsKey("e"));
    assertTrue(table.containsKey("f"));

    assertFalse(table.containsKey("ab"));
    assertFalse(table.containsKey("bc"));
    assertFalse(table.containsKey("cd"));
    assertFalse(table.containsKey("de"));
    assertFalse(table.containsKey("ef"));
    assertFalse(table.containsKey("fg"));
  }

  @Test
  public void testContainsValue() {
    var table = getHashTable();
    assertTrue(table.containsValue(100));
    assertTrue(table.containsValue(400));
    assertTrue(table.containsValue(45));
    assertTrue(table.containsValue(1));

    assertFalse(table.containsValue(111));
    assertFalse(table.containsValue(222));
    assertFalse(table.containsValue(333));
    assertFalse(table.containsValue(444));
  }

  @Test
  public void testPutExisting() {
    var table = getHashTable();
    assertEquals(100, table.put("a", 45));
    assertEquals(2421, table.put("ee", 1));
    assertEquals(45, table.put("a", 3));
  }

  @Test
  public void testPutNonExisting() {
    var table = getHashTable();
    assertNull(table.get("af"));
    table.put("af", 0);
    assertEquals(0, table.get("af"));
  }

  @Test
  public void testRemove() {
    var table = getHashTable();
    assertTrue(table.containsKey("f"));
    table.remove("f");
    assertFalse(table.containsKey("f"));
  }

  @Test
  public void testSize() {
    var table = getHashTable();
    assertEquals(18, table.size());
    table.remove("znj");
    assertEquals(18, table.size());
    table.remove("dd");
    assertEquals(17, table.size());
  }

  @Test
  public void testToArray() {
    var arr = getHashTable().toArray();
    assertEquals(18, arr.length);
  }

  @Test
  public void testGet() {
    var table = getHashTable();
    assertEquals(6093, table.get("f"));
    table.put("f", 452);
    assertEquals(452, table.get("f"));
  }

  @Test
  public void testEmpty() {
    var table = getHashTable();
    assertFalse(table.isEmpty());
    table = new SimpleHashtable<>();
    assertTrue(table.isEmpty());
  }

  @Test
  public void testStandardIteration() {
    var table = getHashTable();
    // checking if all iter vals exist
    for (var e : table) {
      assertTrue(table.containsValue(e.getVal()));
    }
  }

  @Test
  public void testConcurrentModificationIsThrownDuringIteration() {
    // check concurrent exception throws
    var table = getHashTable();
    var iter = table.iterator();
    var e = iter.next();
    table.remove(e.getKey());
    assertThrows(ConcurrentModificationException.class, iter::next);
  }

  @Test
  public void testEmptyHashtableIteratorThrows() {
    // check empty throws
    var iter2 = new SimpleHashtable().iterator();
    assertThrows(NoSuchElementException.class, iter2::next);
  }

  @Test
  public void testIteratorRemoveWorks() {
    // complex check
    var table = getHashTable();
    var arr = table.toArray();
    for (var e : arr) {
      assertTrue(table.containsKey(e.getKey()));
      var it = table.iterator();
      while (it.hasNext()) {
        // only single remove shouldn't throw
        if (it.next().getKey().equals(e.getKey())) {
          it.remove();
          break;
        }
      }
      assertFalse(table.containsKey(e.getKey()));
    }
  }

}