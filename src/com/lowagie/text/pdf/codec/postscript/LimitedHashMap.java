package com.lowagie.text.pdf.codec.postscript;

import java.util.*;

public class LimitedHashMap
    extends LinkedHashMap {
  private int MAX_ENTRIES = 200;
  public LimitedHashMap() {
    super();
  }

  public LimitedHashMap(int MAX_ENTRIES) {
    super();
    this.MAX_ENTRIES=MAX_ENTRIES;
  }

  public LimitedHashMap(int initialCapacity, float loadFactor) {
    super(initialCapacity, loadFactor);
  }

  public LimitedHashMap(int initialCapacity, float loadFactor,
                        boolean accessOrder) {
    super(initialCapacity, loadFactor, accessOrder);
  }

  public LimitedHashMap(Map m) {
    super(m);
  }

  public static void main(String[] args) {
    LimitedHashMap limitedhashmap = new LimitedHashMap();
  }

  /**
   * Associates the specified value with the specified key in this map (optional operation).
   *
   * @param key key with which the specified value is to be associated.
   * @param value value to be associated with the specified key.
   * @return previous value associated with specified key, or <tt>null</tt> if there was no
   *   mapping for key. A <tt>null</tt> return can also indicate that the map previously
   *   associated <tt>null</tt> with the specified key, if the implementation supports
   *   <tt>null</tt> values.
   */
  public Object put(Object key, Object value) {
    if (size() > MAX_ENTRIES) {
      throw new UnsupportedOperationException();
    }
    else {
      return super.put(key, value);
    }
  }
}
