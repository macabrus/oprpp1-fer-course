package hr.fer.oprpp1.custom.collections;

/**
 * Iterator that iterates through object returning single element at a time. Each collection provides its own
 * implementation and returns when {@link Collection#createElementsGetter()} is called.
 */
public interface ElementsGetter<E> {
  /**
   * @return false if end of collection has been reached, true if there are still more elements
   */
  boolean hasNextElement();

  /**
   * @return next element in iteration
   */
  E getNextElement();

  default void processRemaining(Processor<? super E> processor) {
    while(hasNextElement())
      processor.process(getNextElement());
  }
}
