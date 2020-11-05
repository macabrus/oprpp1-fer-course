package hr.fer.oprpp1.custom.collections;

/**
 * An interface that represents some general collection of objects.
 */
public interface Collection<E> {

  /**
   * @return true if collection contains no objects and false otherwise. Implement it here to determine result by
   * utilizing method size()
   */
  default boolean isEmpty() {
    return size() == 0;
  }

  /**
   * @return a number of currently stored objects in this collection
   */
  int size();

  /**
   * Adds the given object into this collection.
   */
  void add(E value);

  /**
   * @return true only if the collection contains given value, as determined by equals method
   */
  boolean contains(Object value);

  /**
   * @return true only if the collection contains given value as determined by equals method and removes one occurrence
   * of it (in this class it is not specified which one)
   */
  boolean remove(Object value);

  /**
   * Allocates new array with size equals to the size of this collection, fills it with collection content and returns
   * the array. This method never returns null.
   *
   * @return an array of collection values
   */
  Object[] toArray();

  /**
   * Method calls {@code processor.process(.)} for each element of this collection. The order in which elements will be
   * sent is undefined in this class.
   */
  default void forEach(Processor<? super E> processor) {
    ElementsGetter<E> getter = createElementsGetter();
    while (getter.hasNextElement())
      processor.process(getter.getNextElement());
  }

  /**
   * Method adds into the current collection all elements from the given collection. This other collection remains
   * unchanged.
   */
  default void addAll(Collection<? extends E> other) {
    other.forEach(this::add);
  }

  /**
   * Adds all elements from othre collection that match some condition to this collection.
   * @param source source collection to add from
   * @param predicate statement to test elements against
   */
  default void addAllSatisfying(Collection<? extends E> source, Tester<? super E> predicate) {
    source.forEach((e) -> {
      if (predicate.test(e))
        this.add(e);
    });
  }

  /**
   * Removes all elements from this collection.
   */
  void clear();

  /**
   * Creates new iterator for this collection.
   * @return an instance of iterator for current collection
   */
  ElementsGetter<E> createElementsGetter();

}
