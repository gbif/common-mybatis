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


import org.gbif.api.vocabulary.Language;

/**
 * Enum converter for {@link Language}.
 */
public class LanguageConverter implements EnumConverter<String, Language> {

  @Override
  public String fromEnum(Language value) {
    return value == null || value == Language.UNKNOWN ? null : value.getIso2LetterCode();
  }

  @Override
  public Language toEnum(String key) {
    return Language.fromIsoCode(key);
  }

}
