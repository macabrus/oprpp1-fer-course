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
    size ++;

    return putInto(key, value, table);
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
      if (iter.key.equals(key))
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

    var it = iterator();
    while (it.hasNext()) {
      if (it.next().getKey().equals(key)) {
        it.remove();
        break;
      }
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
    var sb = new StringBuilder();
    for (int i = 0; i < table.length; i ++) {
      sb.append(i).append(": ");
      var iter = table[i];
      while (iter != null) {
        sb.append(iter).append(", ");
        iter = iter.next;
      }
      sb.append('\n');
    }
    return sb.toString();
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

  @Override
  public Iterator<TableEntry<K, V>> iterator() {
    return new IteratorImpl(this);
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
    // Otherwise, saving old node to tmp var
    node = destination[hash];
    // Adding new item
    destination[hash] = new TableEntry<>(key, value);
    // Re-linking old node and rest of chain
    destination[hash].next = node;

    // Since there was nothing before, returning null
    return null;
  }

  /*---------- KEY-VALUE PAIR ----------*/

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
    public String toString() {
      return  key + " => " + val;
    }
  }

  /*---------- DEFAULT ITERATOR IMPL ----------*/

  private class IteratorImpl implements Iterator<TableEntry<K,V>> {

    // parent hashtable
    private final SimpleHashtable<K, V> binding;

    // last element pointer
    private int prevIndex = -1;
    private TableEntry<K, V> prev = null;

    // current element pointer
    private int currentIndex = -1;
    private TableEntry<K, V> current = null;

    // number of modifications in bound hashtable when iteration started
    private final int savedModificationCount;

    public IteratorImpl(SimpleHashtable<K, V> binding) {
      this.binding = binding;
      this.savedModificationCount = binding.modificationCount;
    }

    @Override
    public boolean hasNext() {
      if (savedModificationCount != binding.modificationCount)
        throw new ConcurrentModificationException("Hashtable" + binding + "changed during iteration over its elements.");

      var tmpIndex = currentIndex + 1;
      var tmp = current;
      if (tmp != null && tmp.next != null)
        return true;
      else if(tmpIndex >= table.length)
        return false;
      // moving to next
      tmp = table[tmpIndex ++];
      // while that next is null, iterate until it finds non-null
      while (tmp == null && tmpIndex < table.length) {
        tmp = table[tmpIndex ++];
      }
      return tmp != null;
    }

    @Override
    public TableEntry<K, V> next() {
      if (savedModificationCount != binding.modificationCount)
        throw new ConcurrentModificationException("Hashtable" + binding + "changed during iteration over its elements.");
      if (currentIndex >= table.length || !hasNext())
        throw new NoSuchElementException("No more elements in collection " + binding);

      moveToNext();

      return current;
    }

    // Moves to next in hashtable, updating previous and current to new values
    private void moveToNext() {
      // capturing current state
      prevIndex = currentIndex;
      prev = current;
      if (current != null && current.next != null)
        current = current.next;
      else {
        // moving to next
        current = table[++ currentIndex];
        // while that next is null, iterate until it finds non-null
        while (current == null && currentIndex + 1 < table.length) {
          current = table[++ currentIndex];
        }
      }
    }

    @Override
    public void remove() {
      // if next() wasn't yet called, call it first and then re-call remove
      if (current == null) {
        next();
        remove();
      }
      // if next() was called first time, prev is still null
      else if (prev == null) {
        table[currentIndex] = table[currentIndex].next;
      }
      else {
        // option 1: same chain
        if (prevIndex == currentIndex)
          prev.next = current.next;
        // option 2: pointer moved to next chain in table
        else
          table[currentIndex] = table[currentIndex].next;
      }
      // since hashtable was modified during iteration,
      // next call would throw exception
      size --;
      binding.modificationCount++;
    }
  }
}
