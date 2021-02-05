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

import org.gbif.api.vocabulary.Language;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Test class for {@link LanguageConverter}.
 */
public class LanguageConverterTest {

  LanguageConverter conv = new LanguageConverter();

  @Test
  public void testCompleteness() {
    for (Language t : Language.values()) {
      if (t == Language.UNKNOWN) {
        assertNull(conv.fromEnum(t));
      } else {
        assertNotNull(conv.fromEnum(t));
      }
    }
  }

  @Test
  public void testToEnum() {
    assertEquals(Language.DANISH, conv.toEnum("DA"));
    assertEquals(Language.DANISH, conv.toEnum("da"));
    assertEquals(Language.ENGLISH, conv.toEnum("EN"));
    assertEquals(Language.SPANISH, conv.toEnum("ES"));
    assertEquals(Language.UNKNOWN, conv.toEnum("ZZ"));
    assertEquals(Language.UNKNOWN, conv.toEnum("nothing"));
    assertEquals(Language.UNKNOWN, conv.toEnum(""));
    assertEquals(Language.UNKNOWN, conv.toEnum(" "));
    assertEquals(Language.UNKNOWN, conv.toEnum(null));
  }

  @Test
  public void testFromEnum() {
    assertEquals("da", conv.fromEnum(Language.DANISH));
    assertNull(conv.fromEnum(Language.UNKNOWN));
    assertNull(conv.fromEnum(null));
  }
}
