package org.gbif.mybatis.type;

import java.util.Map;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;

/**
 * Generic converter class using an immutable bimap for mapping key/values.
 *
 * @param <T> the enumeration to be converted
 */
public abstract class BaseConverter<K, T extends Enum<?>> implements EnumConverter<K, T> {

  private final BiMap<K, T> map;
  private final T nullValue;

  /**
   * @param map       the map of unique persistency integers to unique enum values.
   * @param nullValue the value to be used when a key cannot be found.
   *
   * @throws IllegalArgumentException if two keys have the same value
   * @throws NullPointerException     if any key or value is null
   */
  protected BaseConverter(T nullValue, Map<K, T> map) {
    this.map = ImmutableBiMap.copyOf(map);
    this.nullValue = nullValue;
  }

  @Override
  public T toEnum(K key) {
    if (key == null) return nullValue;
    T val = map.get(key);
    return val == null ? nullValue : val;
  }

  @Override
  public K fromEnum(T value) {
    if (value == null) return null;
    if (map.containsValue(value)) {
      return map.inverse().get(value);
    }
    throw new IllegalArgumentException("Enumeration value " + value.name() + " unknown");
  }
}
