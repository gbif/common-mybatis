package org.gbif.mybatis.type;


import org.gbif.api.vocabulary.Language;

/**
 * Enum converter for {@link Language}.
 */
public class LanguageConverter implements EnumConverter<String, Language> {

  @Override
  public String fromEnum(Language value) {
    return value == null || value == Language.UNKNOWN  ? null : value.getIso2LetterCode();
  }

  @Override
  public Language toEnum(String key) {
    return Language.fromIsoCode(key);
  }

}
