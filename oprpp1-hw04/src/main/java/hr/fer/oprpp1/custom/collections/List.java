package hr.fer.oprpp1.custom.collections;

public interface List<E> extends Collection<E> {

  Object get(int index);

  void insert(E value, int position);

  int indexOf(Object value);

  void remove(int index);
}
