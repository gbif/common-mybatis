/*
 * Copyright 2021 Global Biodiversity Information Facility (GBIF)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
