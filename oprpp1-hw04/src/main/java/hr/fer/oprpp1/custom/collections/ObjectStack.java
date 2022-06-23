package hr.fer.oprpp1.custom.collections;

public class ObjectStack<T> {

  /**
   * @return true if collection contains no objects and false otherwise. Implement it here to determine result by
   * utilizing method size()
   */
  public boolean isEmpty() {
    return elements.isEmpty();
  }

  /**
   * @return a number of currently stored objects in this collection
   */
  public int size() {
    return elements.size();
  }

  /**
   * Pushes given value on the stack.
   * @param value a value to be added to stack
   * @throws NullPointerException if passed null
   */
  public void push(T value) {
    if (value == null)
      throw new NullPointerException();

    elements.add(value);
  }

  /**
   * Removes last value pushed on stack from stack and returns it.
   * @return popped element from stack
   * @throws EmptyStackException if stack is empty
   */
  public T pop() {
    if (size() == 0)
      throw new EmptyStackException();

    var element = peek();
    elements.remove(elements.size() - 1);

    return element;
  }

  /**
   * Similar to pop; returns last element placed on stack but does not delete it from stack.
   * @return last element on stack
   * @throws EmptyStackException if stack is empty
   */
  public T peek() {
    return elements.get(elements.size() - 1);
  }

  /**
   * Removes all elements from stack.
   */
  void clear() {
    elements.clear();
  }

  private final ArrayIndexedCollection<T> elements = new ArrayIndexedCollection<>();

}
