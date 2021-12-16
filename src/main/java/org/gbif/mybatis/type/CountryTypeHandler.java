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

import org.gbif.api.vocabulary.Country;

import org.apache.ibatis.type.MappedTypes;

/**
 * MyBatis TypeHandler for {@link Country} enum.
 * Persists contries as their upper case 2 letter iso code and uses NULL for the UNKNOWN enumeration.
 * Any unknown code or null string is converted to the UNKNWON enum entry.
 */
@MappedTypes({Country.class})
public class CountryTypeHandler extends BaseEnumTypeHandler<String, Country> {

  public CountryTypeHandler() {
    super(new CountryConverter());
  }

}
