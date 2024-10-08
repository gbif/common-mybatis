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

import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import com.google.common.collect.Lists;

public class StringArrayTypeHandler extends BaseTypeHandler<List<String>> {

  @Override
  public void setNonNullParameter(PreparedStatement ps, int i, List<String> parameter, JdbcType jdbcType) throws SQLException {
    Array array = ps.getConnection().createArrayOf("text", parameter.toArray());
    ps.setArray(i, array);
  }

  @Override
  public List<String> getNullableResult(ResultSet rs, String columnName) throws SQLException {
    return toList(rs.getArray(columnName));
  }

  @Override
  public List<String> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
    return toList(rs.getArray(columnIndex));
  }

  @Override
  public List<String> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
    return toList(cs.getArray(columnIndex));
  }

  private List<String> toList(Array pgArray) throws SQLException {
    if (pgArray == null || pgArray.getArray() == null) return Lists.newArrayList();

    String[] strings = (String[]) pgArray.getArray();
    return containsOnlyNulls(strings) ? Lists.<String>newArrayList() : Lists.newArrayList(strings);
  }

  private boolean containsOnlyNulls(String[] strings) {
    for (String s : strings) {
      if (s != null) {
        return false;
      }
    }
    return true;
  }

}
