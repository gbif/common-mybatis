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

import org.gbif.api.util.VocabularyUtils;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Map;

import javax.annotation.Nullable;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Strings;

/**
 * General enumeration type handler that converts enums into their string name() representation
 * and vice versa. Converting into the enum is case-insensitive.
 *
 * @param <T> the exact enumeration to convert
 */
public abstract class EnumDictTypeHandler<T extends Enum<?>> implements TypeHandler<T> {
  private final Map<String, T> dict;
  private final Class<T> clazz;
  private final T defaultValue;

  /**
   * @param clazz        the enumeration class
   * @param defaultValue the default value to be used if it cannot be converted, can be null
   * @param lookupDict   optional map with additional mappings
   */
  protected EnumDictTypeHandler(Class<T> clazz, @Nullable T defaultValue, @Nullable Map<String, T> lookupDict) {
    dict = lookupDict;
    this.clazz = clazz;
    this.defaultValue = defaultValue;
  }


  @Override
  public void setParameter(PreparedStatement ps, int i, T parameter, JdbcType jdbcType) throws SQLException {
    ps.setObject(i, parameter == null ? null : parameter.name(), Types.CHAR);
  }

  @Override
  public T getResult(ResultSet rs, String columnName) throws SQLException {
    return lookup(rs.getString(columnName));
  }

  @Override
  public T getResult(ResultSet rs, int columnIndex) throws SQLException {
    return lookup(rs.getString(columnIndex));
  }

  @Override
  public T getResult(CallableStatement cs, int columnIndex) throws SQLException {
    return lookup(cs.getString(columnIndex));
  }

  @VisibleForTesting
  protected T lookup(String val) {
    try {
      return VocabularyUtils.lookupEnum(val, clazz);
    } catch (IllegalArgumentException e) {
      if (dict != null && !Strings.isNullOrEmpty(val)) {
        return dict.getOrDefault(val.toLowerCase(), defaultValue);
      }
    }
    return defaultValue;
  }
}
