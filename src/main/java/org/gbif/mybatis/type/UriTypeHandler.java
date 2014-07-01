package org.gbif.mybatis.type;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A simple converter for varchars to URI.
 */
public class UriTypeHandler extends BaseTypeHandler<URI> {
  private static final Logger LOG = LoggerFactory.getLogger(UriTypeHandler.class);

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
    try {
      return val == null ? null : new URI(val);
    } catch (URISyntaxException e) {
      LOG.warn("Invalid URI {}", val, e);
      return null;
    }
  }

}
