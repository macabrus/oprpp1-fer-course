package hr.fer.oprpp1.custom.collections;

import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;

/**
 * An implementation of resizable array-backed collection of objects where duplicate elements are allowed; storage of
 * null references is not allowed.
 */
public class ArrayIndexedCollection<E> implements List<E> {

  /**
   * Creates an {@link ArrayIndexedCollection} with default initial capacity (16).
   */
  public ArrayIndexedCollection() {
    this(16);
  }

  /**
   * Creates an {@link ArrayIndexedCollection} with specified initial capacity.
   *
   * @param initialCapacity initial capacity of backing-array
   */
  public ArrayIndexedCollection(int initialCapacity) {
    elements = new Object[initialCapacity];
    size = 0;
  }

  /**
   * Creates an {@link ArrayIndexedCollection} with initial capacity equal to size of source collection and copies its
   * contents to newly created collection.
   *
   * @param collection source collection to copy elements from
   */
  public ArrayIndexedCollection(Collection<? extends E> collection) {
    // Zero is not important because method is going to fail anyway in delegated constructor
    this(collection, collection == null ? 0 : collection.size());
  }

  /**
   * Creates an {@link ArrayIndexedCollection} with given initial capacity and copies contents from source collection to
   * newly created collection.
   *
   * @throws NullPointerException if source collection is null
   * @throws RuntimeException     if initial cpacity is specified to be smaller than the size of provided source
   */
  public ArrayIndexedCollection(Collection<? extends E> collection, int initialCapacity) {
    if (collection == null)
      throw new NullPointerException("Provided source collection must not be null");
    if (initialCapacity < collection.size())
      throw new RuntimeException(
        String.format("Initial capacity is %s, but collection of size %s was given as a source.",
          initialCapacity,
          collection.size()
        )
      );

    elements = new Object[initialCapacity];
    size = initialCapacity;
    var toCopy = collection.toArray();
    System.arraycopy(toCopy, 0, elements, 0, toCopy.length);
  }

  /**
   * Adds the given object into this collection (reference is added into first empty place in the elements array; if the
   * elements array is full, it is reallocated with double the size).
   *
   * @param value an element to be added to collection
   * @throws NullPointerException if null is added as an element
   */
  @Override
  public void add(E value) {
    if (value == null)
      throw new NullPointerException();

    reallocateLeaving(size, 1);
    elements[size++] = value;
    modificationCount++;
  }

  /**
   * Checks if object is contained within collection using {@link Object#equals}
   *
   * @param value object to check
   * @return true if object is inside collection, false otherwise (or if null was passed)
   */
  @Override
  public boolean contains(Object value) {
    if (value == null)
      return false;
    for (int i = 0; i < size; i++)
      if (elements[i].equals(value))
        return true;
    return false;
  }

  /**
   * Removes object by value from collection.
   *
   * @param value object to look for
   * @return true if object was found and removed, false if not found
   */
  @Override
  public boolean remove(Object value) {
    if (value == null)
      return false;
    for (int i = 0; i < size; i++)
      if (elements[i].equals(value)) {
        remove(i);
        return true;
      }
    return false;
  }

  /**
   * @param index index for which element is fetched
   * @return the object that is stored in backing array at position index
   * @throws IndexOutOfBoundsException if index is invalid
   */
  @SuppressWarnings("unchecked")
  public E get(int index) {
    if (index < 0 || index >= elements.length)
      throw new IndexOutOfBoundsException();

    return (E) elements[index];
  }

  /**
   * Removes all elements from the collection. The allocated array is left at current capacity. Writes null references
   * into the backing array, so that objects which became unreferenced become eligible for garbage collection. Does not
   * allocate new array.
   */
  @Override
  public void clear() {
    Arrays.fill(elements, null);
    size = 0;
  }

  @Override
  public ElementsGetter<E> createElementsGetter() {
    return new ElementsGetterImpl<>(this);
  }

  /**
   * Inserts (does not overwrite) the given value at the given position in array while shifting subsequent members one
   * place to the right.
   *
   * @param value    a value to be inserted
   * @param position a number between 0 to size (both are included)
   * @throws IndexOutOfBoundsException if {@code position} is invalid
   */
  @Override
  public void insert(E value, int position) {
    if (position < 0 || position > size)
      throw new IndexOutOfBoundsException();

    reallocateLeaving(position, 1);
    elements[position] = value;
    size++;
    modificationCount++;
  }

  /**
   * Searches the collection and returns the index of the first occurrence of the given value or -1 if the value is not
   * found. Argument can be null and the result must be that this element is not found (since the collection can not
   * contain null). The equality should be determined using the equals method.
   *
   * @return an index of given element
   */
  public int indexOf(Object value) {
    if (value == null)
      return -1;
    for (int i = 0; i < size; i++)
      if (elements[i].equals(value))
        return i;
    return -1;
  }

  /**
   * Removes element at specified index from collection. Subsequent elements are shifted one position back. Please note
   * that the method remove(Object value) specified in class Collection and this method are two different methods which
   * perform different operations.
   *
   * @param index an index of element to be removed
   * @throws IndexOutOfBoundsException if index is invalid ({@code index < 0 || index >= elements.length})
   */
  public void remove(int index) {
    if (index < 0 || index >= elements.length)
      throw new IndexOutOfBoundsException();

    System.arraycopy(elements, index + 1, elements, index, size - index);
    elements[--size] = null;
    modificationCount++;
  }

  /**
   * @return current number of elements in collection
   */
  @Override
  public int size() {
    return size;
  }

  /**
   * Converts collection to primitive array of {@link Object}.
   *
   * @return primitive array of collection elements
   */
  @Override
  public Object[] toArray() {
    return Arrays.copyOf(elements, size);
  }

  /*---------- PRIVATE MEMBERS ----------*/

  /**
   * Reallocates backing array to new array with size equal to the newSize for which the following condition is met:
   * {@code size + length > newSize}
   *
   * @param offset a number of elements that are skipped before giving offset
   * @param length a length of empty block in new array successive half of original array
   * @throws IndexOutOfBoundsException if {@code offset} is larger than original array
   */
  private void reallocateLeaving(int offset, int length) {
    if (offset > size)
      throw new IndexOutOfBoundsException();

    // Reallocating array if necessary
    int newSize = elements.length;
    while (size + length > newSize)
      newSize *= 2;
    var tmp = elements;
    elements = newSize != elements.length ? (E[]) new Object[newSize] : elements;

    // Copying to new array (or old one if there was enough room)
    System.arraycopy(tmp, 0, elements, 0, offset);
    System.arraycopy(tmp, offset, elements, offset + length, size - offset);
  }

  /**
   * current size of collection (number of elements actually stored in elements array)
   */
  private int size;

  /**
   * an array of object references which length is determined by capacity variable
   */
  private Object[] elements;

  private int modificationCount;

  /**
   * Basic implementation of {@link ElementsGetter} interface tailored for this collection.
   */
  private static class ElementsGetterImpl<E> implements ElementsGetter<E> {

    private final int savedModificationCount;
    private int index;
    private final ArrayIndexedCollection<E> binding;

    private ElementsGetterImpl(ArrayIndexedCollection<E> binding) {
      this.binding = binding;
      savedModificationCount = binding.modificationCount;
    }

    @Override
    public boolean hasNextElement() {
      if (savedModificationCount != binding.modificationCount)
        throw new ConcurrentModificationException("Collection was modified while " + this + " was iterating.");

      return index < binding.size;
    }

    @Override
    public E getNextElement() {
      if (hasNextElement())
        return binding.get(index++);

      throw new NoSuchElementException("No more elements in collection " + binding);
    }
  }

}
