package org.gbif.mybatis.type;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.UUID;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

/**
 * Handler for UUID types.
 *
 * @see UUID
 */
public class UuidTypeHandler implements TypeHandler<UUID> {

  @Override
  public void setParameter(PreparedStatement ps, int i, UUID parameter, JdbcType jdbcType) throws SQLException {
    if (parameter == null) {
      ps.setObject(i, null, Types.OTHER);
    } else {
      ps.setObject(i, parameter.toString(), Types.OTHER);
    }

  }

  @Override
  public UUID getResult(ResultSet rs, String columnName) throws SQLException {
    if (rs.getString(columnName) != null) {
      return UUID.fromString(rs.getString(columnName));
    }
    return null;
  }

  @Override
  public UUID getResult(ResultSet rs, int columnIndex) throws SQLException {
    return UUID.fromString(rs.getString(columnIndex));
  }

  @Override
  public UUID getResult(CallableStatement cs, int columnIndex) throws SQLException {
    return UUID.fromString(cs.getString(columnIndex));
  }

}
