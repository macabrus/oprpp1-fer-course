package hr.fer.oprpp1.custom.collections;

import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;

public class LinkedListIndexedCollection<E> implements List<E> {

  public LinkedListIndexedCollection() {
    first = last = null;
  }

  public LinkedListIndexedCollection(Collection<? extends E> source) {
    Arrays.stream(source.toArray()).forEach(this::add);
  }

  /**
   * Fetches an element from list at given index.
   *
   * @param index a value between {@code 0} and {@code size - 1}
   * @return the object that is stored in linked list at position index
   * @throws IndexOutOfBoundsException if invalid index is passed
   */
  public E get(int index) {
    if (index >= size || index < 0)
      throw new IndexOutOfBoundsException();

    return getNode(index).value;
  }

  /**
   * Inserts (does not overwrite) the given value at the given position in linked-list. Elements starting from this
   * position are shifted one position.
   *
   * @param position a value between {@code 0} and {@code size}
   * @throws IndexOutOfBoundsException if invalid {@code position} is passed
   */
  public void insert(E value, int position) {
    if (position < 0 || position > size)
      throw new IndexOutOfBoundsException();
    if (position == size) {
      add(value);
      return;
    }

    var currentNode = first;
    var newNode = new ListNode<E>(value);
    for (int i = 0; i < position; i++)
      currentNode = currentNode.next;
    link(newNode, currentNode.previous, currentNode);
  }

  /**
   * Searches the collection and returns the index of the first occurrence of the given value or -1 if the value is not
   * found. The equality should be determined using the equals method.
   *
   * @param value value to search for (null is a valid argument)
   * @return an index of {@link ListNode} with {@link ListNode#value} equal to passed object
   */
  public int indexOf(Object value) {
    if (value == null)
      return -1;

    int i = 0;
    var currentNode = first;
    while (i++ < size)
      if (currentNode.value.equals(value))
        return i - 1;
    return -1;
  }

  /**
   * Removes element at specified index from collection. Element that was previously at location index+1 after this
   * operation is on location index, etc.
   *
   * @param index a number between {@code 0} and {@code size - 1}
   * @throws IndexOutOfBoundsException if index is invalid
   */
  public void remove(int index) {
    if (index < 0 || index > size - 1)
      throw new IndexOutOfBoundsException();

    int i = 0;
    var currentNode = first;
    while (i++ < index)
      currentNode = currentNode.next;
    unlink(currentNode);
  }

  /**
   * Safely fetches {@link ListNode} at given index in list.
   *
   * @param index position of node to be fetched
   * @return {@link ListNode} at given position in linked list
   */
  private ListNode<E> getNode(int index) {
    if (index <= size / 2) {
      int i = 0;
      var currentNode = first;
      while (i++ < index)
        currentNode = currentNode.next;
      return currentNode;
    } else {
      int i = size;
      var currentNode = last;
      while (i-- > index)
        currentNode = currentNode.previous;
      return currentNode;
    }
  }

  /*---------- OVERRIDES ----------*/

  /**
   * Adds the given object into this collection at the end of collection; newly added element becomes the element at the
   * biggest index (tail).
   *
   * @param value object to insert into list
   * @throws NullPointerException if passed null value
   */
  @Override
  public void add(E value) {
    if (value == null)
      throw new NullPointerException();
    link(new ListNode<>(value), last, null);
  }

  @Override
  public boolean contains(Object value) {
    if (value == null)
      return false;
    var node = first;
    while (node != null) {
      if (node.value.equals(value))
        return true;
      node = node.next;
    }
    return false;
  }

  @Override
  public int size() {
    return size;
  }

  /**
   * Converts list to array.
   *
   * @return an array representation of this list.
   */
  @Override
  public E[] toArray() {
    System.out.println("VELIČINA JE " + size);
    var arr = new Object[size];
    var n = first;
    int i = 0;
    while (n != null) {
      arr[i++] = n.value;
      n = n.next;
    }
    return (E[]) arr;
  }

  /**
   * Removes first occurence of {@link ListNode} with given value.
   *
   * @param value value to check against
   * @return true if element was removed, false otherwise
   */
  @Override
  public boolean remove(Object value) {
    if (value == null)
      return false;

    var currentNode = first;
    for (int i = 0; i < size; i++) {
      if (currentNode.value.equals(value)) {
        unlink(currentNode);
        return true;
      }
      currentNode = currentNode.next;
    }
    return false;
  }

  /**
   * Removes all elements from the collection. Collection "forgets" about current linked list.
   */
  @Override
  public void clear() {
    first = last = null;
    size = 0;
  }

  @Override
  public ElementsGetter<E> createElementsGetter() {
    return new ElementsGetterImpl<>(this);
  }

  /*----------  PRIVATE MEMBERS ----------*/

  /**
   * Safely links passed node to its peers.
   *
   * @param node a {@link ListNode} to be inserted between {@code prev} & {@code next}
   * @param prev a {@link ListNode} that will now point to {@code node} as its successor
   * @param next a {@link ListNode} that will now point to {@code node} as its predecessor
   */
  private void link(ListNode<E> node, ListNode<E> prev, ListNode<E> next) {
    if (node == null)
      return;
    if (next == first)
      first = node;
    if (prev == last)
      last = node;
    if (prev != null) {
      prev.next = node;
      node.previous = prev;
    }
    if (next != null) {
      next.previous = node;
      node.next = next;
    }
    size++;
  }

  /**
   * Safely unlinks passed node from its peers.
   *
   * @param node a {@link ListNode} to unlink
   */
  private void unlink(ListNode<E> node) {
    if (node == null)
      return;
    if (node == first)
      first = node.next;
    if (node == last)
      last = node.previous;
    if (node.next != null)
      node.next.previous = node.previous;
    if (node.previous != null)
      node.previous.next = node.next;
    size--;
  }

  /**
   * current size of linked list
   */
  private int size;

  /**
   * first element in a list (head)
   */
  private ListNode<E> first;

  /**
   * last element in a list (tail)
   */
  private ListNode<E> last;

  /**
   * a node object in linked list; contains reference to previous, next node and holds a reference to stored value.
   */
  private static class ListNode<E> {
    private ListNode<E> previous;
    private ListNode<E> next;
    public E value;

    public ListNode(E value) {
      this.value = value;
    }
  }

  private int modificationCount;

  /**
   * Basic implementation of {@link ElementsGetter} interface tailored for this collection.
   */
  private static class ElementsGetterImpl<E> implements ElementsGetter<E> {

    private final int savedModificationCount;
    private ListNode<E> currentNode;
    private final LinkedListIndexedCollection<E> binding;

    private ElementsGetterImpl(LinkedListIndexedCollection<E> binding) {
      this.binding = binding;
      this.currentNode = binding.first;
      this.savedModificationCount = binding.modificationCount;
    }

    @Override
    public boolean hasNextElement() {
      if (savedModificationCount != binding.modificationCount)
        throw new ConcurrentModificationException("Collection was modified while " + this + " was iterating.");

      return currentNode != null;
    }

    @Override
    public E getNextElement() {
      if (hasNextElement()) {
        var tmp = currentNode;
        currentNode = currentNode.next;
        return tmp.value;
      }
      throw new NoSuchElementException("No more elements in collection " + binding);
    }
  }
}
