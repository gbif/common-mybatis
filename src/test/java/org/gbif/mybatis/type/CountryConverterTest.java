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

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class CountryConverterTest {

  private final CountryConverter conv = new CountryConverter();

  @Test
  public void testCompleteness() {
    for (Country t : Country.values()) {
      if (t == Country.UNKNOWN) {
        assertNull(conv.fromEnum(t));
      } else {
        assertNotNull(conv.fromEnum(t));
      }
    }
  }

  @Test
  public void testToEnum() {
    assertEquals(Country.DENMARK, conv.toEnum("DK"));
    assertEquals(Country.DENMARK, conv.toEnum("dk"));
    assertEquals(Country.UNITED_STATES, conv.toEnum("US"));
    assertEquals(Country.UNITED_STATES, conv.toEnum("us"));
    assertEquals(Country.COSTA_RICA, conv.toEnum("CR"));
    assertEquals(Country.COSTA_RICA, conv.toEnum("cr"));
    assertEquals(Country.COSTA_RICA, conv.toEnum("CRI"));
    assertEquals(Country.COSTA_RICA, conv.toEnum("cri"));
    assertEquals(Country.UNKNOWN, conv.toEnum(""));
    assertEquals(Country.UNKNOWN, conv.toEnum(" "));
    assertNull(conv.toEnum(null));
    assertEquals(Country.UNKNOWN, conv.toEnum("zz"));
  }

  @Test
  public void testFromEnum() {
    assertNull(conv.fromEnum(null));
    assertNull(conv.fromEnum(Country.UNKNOWN));
    assertEquals("US", conv.fromEnum(Country.UNITED_STATES));
    assertEquals("CR", conv.fromEnum(Country.COSTA_RICA));
  }
}
