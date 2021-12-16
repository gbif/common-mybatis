/*
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

/**
 * Generic enumeration value converter interface for mapping magic keys used for persisting
 * to their respective enum values and vice versa.
 *
 * @param <K> the specific key, usually Integer or String this converter is working with
 * @param <T> the specific enumeration this converter is working with
 */
public interface EnumConverter<K, T extends Enum<?>> {

  /**
   * Converts an integer key used for persisting to the matching enumeration value.
   *
   * @param key the (term) id used for persisting
   * @return the matching enumeration value or NULL if not found
   */
  T toEnum(K key);

  /**
   * Converts an enumeration value to its unique integer key used for persisting.
   *
   * @param value the enumeration value
   * @return the matching (term) id used for persisting
   * @throws IllegalArgumentException for unknown enumeration values
   */
  K fromEnum(T value);
}
