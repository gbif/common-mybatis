package org.gbif.mybatis.type;

import org.apache.ibatis.type.MappedTypes;
import org.gbif.api.vocabulary.Language;

/**
 * MyBatis type handler for {@link Language}.
 * Persists languages as their lower case 2 letter iso code and uses NULL for the UNKNOWN enumeration.
 * Any unknown code or null string is converted to the UNKNWON enum entry.
 */
@MappedTypes({Language.class})
public class LanguageTypeHandler extends BaseEnumTypeHandler<String, Language> {

  public LanguageTypeHandler() {
    super(new LanguageConverter());
  }

}
