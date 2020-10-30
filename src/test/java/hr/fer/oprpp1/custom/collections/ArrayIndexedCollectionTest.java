package hr.fer.oprpp1.custom.collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class ArrayIndexedCollectionTest {

  private ArrayIndexedCollection<Integer> arr;

  @BeforeEach
  public void setup() {
    arr = new ArrayIndexedCollection<>();
  }

  @Test
  public void addToArrayIndexedCollection() {
    assertEquals(0, arr.size());
    var expected = new Integer[65];
    for (int i = 0; i < 65; i++) {
      arr.add(i);
      expected[i] = i;
    }
    assertEquals(65, arr.size());
    assertArrayEquals(expected, arr.toArray());
  }

  @Test
  public void removeFromArrayIndexedCollection() {
    addToArrayIndexedCollection();
    for (int i = 0; i < 65; i++) {
      arr.remove(0);
    }
    assertEquals(0, arr.size());
  }

  @Test
  public void insertIntoArrayIndexedCollection() {
    addToArrayIndexedCollection();
    for (int i = 0; i < 65; i++) {
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

    assertArrayEquals(new Object[]{0, 1, 4, 3, 2}, arr.toArray());
  }

  @Test
  public void constructFromExisting() {
    var src = new LinkedListIndexedCollection<Integer>();
    src.add(1);
    src.add(2);
    src.add(3);
    arr = new ArrayIndexedCollection<>(src);
    assertArrayEquals(new Object[]{1, 2, 3}, arr.toArray());
  }

  @Test
  public void constructorThrows() {
    assertThrows(NullPointerException.class, () -> new ArrayIndexedCollection<>(null));
    assertThrows(RuntimeException.class, () -> {
      ArrayIndexedCollection<Integer> source = new ArrayIndexedCollection<>();
      source.add(1);
      source.add(2);
      source.add(3);
      new ArrayIndexedCollection<>(source, 1);
    });
  }

  @Test
  public void addNullThrows() {
    assertThrows(NullPointerException.class, () -> arr.add(null));
  }

  @Test
  public void elementsGetterTest() {
    var arr = new ArrayIndexedCollection<String>();
    arr.add("Ivo");
    arr.add("Ana");
    arr.add("Jasna");
    ElementsGetter<String> getter = arr.createElementsGetter();
    assertTrue(getter.hasNextElement());
    assertEquals("Ivo", getter.getNextElement());
    assertTrue(getter.hasNextElement());
    assertEquals("Ana", getter.getNextElement());
    assertTrue(getter.hasNextElement());
    assertEquals("Jasna", getter.getNextElement());
    assertFalse(getter.hasNextElement());
    assertThrows(NoSuchElementException.class, getter::getNextElement);
  }

  @Test
  public void hasNextShouldntModifyCollection() {
    var arr = new ArrayIndexedCollection<String>();
    arr.add("Ivo");
    arr.add("Ana");
    arr.add("Jasna");
    ElementsGetter<String> getter = arr.createElementsGetter();
    assertTrue(getter.hasNextElement());
    assertTrue(getter.hasNextElement());
    assertTrue(getter.hasNextElement());
    assertTrue(getter.hasNextElement());
    assertTrue(getter.hasNextElement());
    assertEquals("Ivo", getter.getNextElement());
    assertTrue(getter.hasNextElement());
    assertTrue(getter.hasNextElement());
    assertEquals("Ana", getter.getNextElement());
    assertTrue(getter.hasNextElement());
    assertTrue(getter.hasNextElement());
    assertTrue(getter.hasNextElement());
    assertEquals("Jasna", getter.getNextElement());
    assertFalse(getter.hasNextElement());
    assertFalse(getter.hasNextElement());
  }

  @Test
  public void elementsGetterWorksWithoutCheckingForNext() {
    var arr = new ArrayIndexedCollection<String>();
    arr.add("Ivo");
    arr.add("Ana");
    arr.add("Jasna");
    ElementsGetter<String> getter = arr.createElementsGetter();
    assertEquals("Ivo", getter.getNextElement());
    assertEquals("Ana", getter.getNextElement());
    assertEquals("Jasna", getter.getNextElement());
    assertThrows(NoSuchElementException.class, getter::getNextElement);
  }

  @Test
  public void allowsMultipleElementGetterInstances() {
    var arr = new ArrayIndexedCollection<String>();
    arr.add("Ivo");
    arr.add("Ana");
    arr.add("Jasna");
    ElementsGetter<String> getter1 = arr.createElementsGetter();
    ElementsGetter<String> getter2 = arr.createElementsGetter();
    assertEquals("Ivo", getter1.getNextElement());
    assertEquals("Ana", getter1.getNextElement());
    assertEquals("Ivo", getter2.getNextElement());
    assertEquals("Jasna", getter1.getNextElement());
    assertEquals("Ana", getter2.getNextElement());
  }

  @Test
  public void checkIndependenceOfElementsGetterInstancesBetweenTwoCollections() {
    Collection<String> col1 = new ArrayIndexedCollection<>();
    Collection<String> col2 = new ArrayIndexedCollection<>();
    col1.add("Ivo");
    col1.add("Ana");
    col1.add("Jasna");
    col2.add("Jasmina");
    col2.add("Štefanija");
    col2.add("Karmela");
    ElementsGetter<String> getter1 = col1.createElementsGetter();
    ElementsGetter<String> getter2 = col1.createElementsGetter();
    ElementsGetter<String> getter3 = col2.createElementsGetter();
    // Ivo, Ana, Ivo, Jasmina, Štefanija
    assertEquals("Ivo", getter1.getNextElement());
    assertEquals("Ana", getter1.getNextElement());
    assertEquals("Ivo", getter2.getNextElement());
    assertEquals("Jasmina", getter3.getNextElement());
    assertEquals("Štefanija", getter3.getNextElement());
  }

  @Test
  public void processRemainingWorkd() {
    var col = new ArrayIndexedCollection<String>();
    col.add("Ivo");
    col.add("Ana");
    col.add("Jasna");
    ElementsGetter<String> getter = col.createElementsGetter();
    getter.getNextElement();
    var col2 = new ArrayIndexedCollection<>();
    col2.add("Ivo");
    getter.processRemaining(col2::add);
    assertArrayEquals(col.toArray(), col2.toArray());
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
    var col = new ArrayIndexedCollection<A>();
    col.add(a);
    col.add(b);
    col.toArray();
    col.forEach(System.out::println);
    col.forEach((Object o) -> { });
    assertFalse(col.contains("test"));
    assertTrue(col.contains(b));
    assertArrayEquals(arr, col.toArray());
    assertArrayEquals(arr, new ArrayIndexedCollection<A>(col).toArray());
  }


}