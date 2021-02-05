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

import org.gbif.api.vocabulary.Country;

/**
 * Enum converter for {@link Country}.
 */
public class CountryConverter implements EnumConverter<String, Country> {

  @Override
  public String fromEnum(Country value) {
    return value == null || value == Country.UNKNOWN ? null : value.getIso2LetterCode();
  }

  /**
   * Matches a notnull key against the Country Enum. A key with no match against the country Enum will return
   * Country.UNKNOWN.
   *
   * @param key a 2 or 3 letter ISO 3166 Country code
   * @return matching Country Enum, or null if incoming key was null
   */
  @Override
  public Country toEnum(String key) {
    if (key == null) {
      return null;
    } else {
      Country c = Country.fromIsoCode(key);
      return c == null ? Country.UNKNOWN : c;
    }
  }
}
