package hr.fer.oprpp1.custom.collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class LinkedListIndexedCollectionTest {

  private LinkedListIndexedCollection<Integer> arr;

  @BeforeEach
  public void setup() {
    arr = new LinkedListIndexedCollection<>();
  }

  @Test
  public void addToLinkedListIndexedCollection() {
    assertEquals(0, arr.size());
    for (int i = 0; i < 65; i++) {
      arr.add(i);
    }
    assertEquals(65, arr.size());
  }

  @Test
  public void removeFromLinkedListIndexedCollection() {
    addToLinkedListIndexedCollection();
    for (int i = 0; i < 65; i++) {
      arr.remove(0);
    }
    assertEquals(0, arr.size());
  }

  @Test
  public void insertIntoLinkedListIndexedCollection() {
    addToLinkedListIndexedCollection();
    for(int i = 0; i < 65; i++) {
      arr.insert(i, 65);
    }
    assertEquals(130, arr.size());
  }

  @Test
  public void correctOrdering() {
    arr.insert(0, 0);
    arr.add(1);
    arr.add(2);
    arr.insert(3, 2);
    arr.insert(4, 2);
    arr.insert(5, 0);
    assertArrayEquals(new Object[]{5, 0, 1, 4, 3, 2}, arr.toArray());
    arr.remove(3);
    assertArrayEquals(new Object[]{5, 0, 1, 3, 2}, arr.toArray());
    arr.remove(4);
    assertArrayEquals(new Object[]{5, 0, 1, 3}, arr.toArray());
    arr.remove(0);
    assertArrayEquals(new Object[]{0, 1, 3}, arr.toArray());
    arr.remove(0);arr.remove(0);arr.remove(0);
    assertArrayEquals(new Object[0], arr.toArray());
  }

  @Test
  public void constructFromExisting() {
    var src = new ArrayIndexedCollection<Integer>();
    src.add(1);
    src.add(2);
    src.add(3);
    arr = new LinkedListIndexedCollection<>(src);
    assertArrayEquals(new Object[]{1,2,3} ,arr.toArray());
  }

  @Test
  public void addNullThrows() {
    assertThrows(NullPointerException.class, () -> arr.add(null));
  }

  /*---------- GENERICS TESTS ----------*/

  private static class A { }
  private static class B extends A {}
  private static class C extends B {}

  @Test
  public void testGenerics() {
    var a = new A();
    var b = new B();
    var arr = new Object[] {a,b,};
    var col = new LinkedListIndexedCollection<A>();
    col.add(a);
    col.add(b);
    col.forEach((Object o) -> { });
    assertFalse(col.contains("test"));
    assertTrue(col.contains(b));
    assertArrayEquals(arr, col.toArray());
    assertArrayEquals(arr, new LinkedListIndexedCollection<>(col).toArray());
  }

}