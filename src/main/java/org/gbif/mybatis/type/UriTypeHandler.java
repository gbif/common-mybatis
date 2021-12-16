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

import org.gbif.common.parsers.UrlParser;

import java.net.URI;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

/**
 * A simple converter for varchars to URI.
 */
@MappedTypes({URI.class})
public class UriTypeHandler extends BaseTypeHandler<URI> {

  @Override
  public void setNonNullParameter(PreparedStatement ps, int i, URI parameter, JdbcType jdbcType) throws SQLException {
    ps.setString(i, parameter.toString());
  }

  @Override
  public URI getNullableResult(ResultSet rs, String columnName) throws SQLException {
    return toURI(rs.getString(columnName));
  }

  @Override
  public URI getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
    return toURI(rs.getString(columnIndex));
  }

  @Override
  public URI getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
    return toURI(cs.getString(columnIndex));
  }

  private static URI toURI(String val) throws SQLException {
    return UrlParser.parse(val);
  }

}
