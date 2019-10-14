package org.gbif.mybatis.type;

import org.apache.ibatis.type.MappedTypes;
import org.gbif.api.vocabulary.Country;

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
