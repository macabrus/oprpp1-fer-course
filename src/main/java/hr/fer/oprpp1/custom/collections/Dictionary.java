package hr.fer.oprpp1.custom.collections;

import java.util.Objects;

public class Dictionary<K, V> {

  private final ArrayIndexedCollection<Pair<K,V>> elements = new ArrayIndexedCollection<>();

  public boolean isEmpty() {
    return elements.isEmpty();
  }

  public int size() {
    return elements.size();
  }

  public void clear() {
    elements.clear();
  }

  public V put(K key, V value) {

    if (key == null)
      throw new NullPointerException("Key can't be null");

    var iter = elements.createElementsGetter();
    V ret = null;

    while (iter.hasNextElement()) {
      var elem = iter.getNextElement();
      if (elem.key.equals(key)) {
        ret = elem.getVal();
        break;
      }
    }
    elements.add(new Pair<>(key, value));

    return ret;
  }

  public V get(Object key) {
    var iter = elements.createElementsGetter();
    while (iter.hasNextElement()) {
      var elem = iter.getNextElement();
      if (elem.key.equals(key)) {
        return elem.getVal();
      }
    }
    return null;
  }

  public V remove(K key) {
    for(int i = 0; i < elements.size(); i++) {
      var e = elements.get(i);
      if (e.getKey().equals(key)) {
        elements.remove(i);
        return e.getVal();
      }
    }
    return null;
  }

  private static class Pair<K,V> {
    private K key;
    private V val;
    public Pair(K key, V val) {
      this.key = key;
      this.val = val;
    }

    public K getKey() {
      return key;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Pair<?, ?> pair = (Pair<?, ?>) o;
      return Objects.equals(key, pair.key) &&
        Objects.equals(val, pair.val);
    }

    @Override
    public int hashCode() {
      return Objects.hash(key, val);
    }

    public V getVal() {
      return val;
    }
  }
}
