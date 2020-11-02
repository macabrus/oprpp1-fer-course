package hr.fer.oprpp1.custom.hashtables;

import java.util.*;

// Maps are not collections.
public class SimpleHashtable<K, V> implements Iterable<SimpleHashtable.TableEntry<K,V>> {

  public SimpleHashtable() {
    table = (TableEntry<K, V>[]) new TableEntry[16];
  }

  public SimpleHashtable(int capacity) {
    if (capacity < 1)
      throw new IllegalArgumentException("Initial capacity must be at least 1");

    if ((capacity & (capacity - 1)) == 0)
      table = (TableEntry<K, V>[]) new TableEntry[capacity];
    else
      table = (TableEntry<K, V>[]) new TableEntry[(int) Math.pow(2, Math.ceil(Math.log(capacity) / Math.log(2)))];
  }

  public V put(K key, V value) {
    if (key == null)
      throw new NullPointerException();
    if (((double) size) / table.length >= 0.75)
      reallocate(table.length * 2);

    // Modification started
    modificationCount ++;
    // avoiding computing expensive hashes twice
    var hash = Math.abs(key.hashCode()) % table.length;
    // Temporary holder for old value
    var node = table[hash];
    while (node != null) {
      if (node.getKey().equals(key)) {
        var tmp = node.val;
        node.val = value;
        return tmp;
      }
      node = node.next;
    }
    // Otherwise, adding new node to chain
    node = table[hash];
    // Adding new item
    table[hash] = new TableEntry<>(key, value);
    // Re-linking rest of chain
    table[hash].next = node;

    // Incrementing size since new entry WAS added
    size ++;
    // Since there was nothing before, returning null
    return null;
  }

  public V get(Object key) {
    if (key == null)
      return null;

    var hash = Math.abs(key.hashCode()) % table.length;
    var node = table[hash];
    while (node != null) {
      if (node.getKey().equals(key)) {
        return node.val;
      }
      node = node.next;
    }

    return null;
  }

  public int size() {
    return size;
  }

  public boolean containsKey(Object key) {
    if (key == null)
      return false;

    var hash = Math.abs(key.hashCode()) % table.length;
    var iter = table[hash];
    while (iter != null) {
      if (iter.key == key)
        return true;
      iter = iter.next;
    }

    return false;
  }

  public boolean containsValue(Object value) {
    for (TableEntry<K, V> e : table) {
      var iter = e;
      while (iter != null) {
        // Null safe checks
        if ((iter.val != null && iter.val.equals(value)) ||
            (value != null && value.equals(iter.val)))
          return true;
        iter = iter.next;
      }
    }

    return false;
  }

  public V remove(Object key) {
    if (key == null)
      return null;

    // Modification started
    modificationCount ++;

    var hash = Math.abs(key.hashCode()) % table.length;
    var iter = table[hash];
    TableEntry<K, V> prev = null;
    while (iter != null) {
      // Keys can't be null so this is safe
      if (iter.key.equals(key)) {
        // Saving value
        var val = iter.val;
        // If first in linked list, there is not yet previous
        if (prev == null)
          table[hash] = iter.next;
        // Else, previous' next is current's next
        else
          prev.next = iter.next;
        // Decrementing size
        size --;
        // returning old value
        return val;
      }
      // Moving two pointers to next
      prev = iter;
      iter = iter.next;
    }

    // No such key found, nothing was deleted
    return null;
  }

  public boolean isEmpty() {
    return size == 0;
  }

  public void clear() {
    Arrays.fill(table, null);
    size = 0;
    modificationCount ++;
  }

  // for debug purposes
  public String toString() {
    for (int i = 0; i < table.length; i ++) {
      System.out.print(i + ": ");
      var iter = table[i];
      while (iter != null) {
        System.out.print(iter + ", ");
        iter = iter.next;
      }
      System.out.println("");
    }
    return null;
  }

  public TableEntry<K,V>[] toArray() {
    int j = 0;
    TableEntry<K, V>[] arr = (TableEntry<K, V>[]) new TableEntry[size];
    for (int i = 0; i < table.length; i ++) {
      var iter = table[i];
      while (iter != null) {
        arr[j++] = iter;
        iter = iter.next;
      }
    }

    return arr;
  }

  /*---------- PRIVATE MEMBERS ----------*/

  private TableEntry<K,V>[] table;

  private int size = 0;

  private int modificationCount = 0;

  private void reallocate(int newSize) {
    var newTable = new TableEntry[newSize];
    for (TableEntry<K, V> e : toArray())
      putInto(e.key, e.val, newTable);
    table = newTable;
  }

  private V putInto(K key, V value, TableEntry<K, V>[] destination) {
    // avoiding computing expensive hashes twice
    var hash = Math.abs(key.hashCode()) % destination.length;
    // Temporary holder for old value
    var node = destination[hash];
    while (node != null) {
      if (node.getKey().equals(key)) {
        var tmp = node.val;
        node.val = value;
        return tmp;
      }
      node = node.next;
    }
    // Otherwise, adding new node to chain
    node = destination[hash];
    // Adding new item
    destination[hash] = new TableEntry<>(key, value);
    // Re-linking rest of chain
    destination[hash].next = node;

    // Since there was nothing before, returning null
    return null;
  }

  @Override
  public Iterator<TableEntry<K, V>> iterator() {
    return new IteratorImpl(this);
  }

  public static class TableEntry<K,V> {
    private K key;
    private V val;
    private TableEntry<K,V> next;

    public TableEntry(K key, V val) {
      this.key = key;
      this.val = val;
    }

    public K getKey() {
      return key;
    }

    public V getVal() {
      return val;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      TableEntry<?, ?> pair = (TableEntry<?,?>) o;
      return Objects.equals(key, pair.key) &&
        Objects.equals(val, pair.val);
    }

    @Override
    public int hashCode() {
      return Objects.hash(key, val);
    }

    @Override
    public String toString() {
      return  key + " => " + val;
    }
  }

  private class IteratorImpl implements Iterator<TableEntry<K,V>> {

    private SimpleHashtable<K, V> binding;
    private int index = 0;
    private TableEntry<K, V> currentNode = null;
    private final int savedModificationCount;

    public IteratorImpl(SimpleHashtable<K, V> binding) {
      this.binding = binding;
      savedModificationCount = binding.modificationCount;
    }

    @Override
    public boolean hasNext() {
      if (savedModificationCount != binding.modificationCount)
        throw new ConcurrentModificationException("Hashtable" + binding + "changed during iteration over its elements.");

      var tmp = currentNode;
      if (tmp == null || tmp.next == null) {
        while (tmp == null && index < table.length)
          tmp = table[index++];
        return tmp == null;
      }
      return false;
    }

    @Override
    public TableEntry<K, V> next() {
      if (savedModificationCount != binding.modificationCount)
        throw new ConcurrentModificationException("Hashtable" + binding + "changed during iteration over its elements.");
      if (index >= table.length || !hasNext())
        throw new NoSuchElementException("No more elements in collection " + binding);

      if (currentNode == null || currentNode.next == null)
        while (index < table.length && currentNode == null)
          currentNode = table[index++];
      else
        currentNode = currentNode.next;

      return currentNode;
    }

    public void remove() {

    }
  }
}
