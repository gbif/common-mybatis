package org.gbif.mybatis.type;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Base handler for all Enums from the term_type table that are backed by the preferred terms of the term table.
 *
 * @param <T> the enumeration to be handled
 */
public class BaseEnumTypeHandler<K, T extends Enum<?>> implements TypeHandler<T> {

  private final EnumConverter<K, T> typeConverter;

  public BaseEnumTypeHandler(EnumConverter<K, T> typeConverter) {
    this.typeConverter = typeConverter;
  }

  @Override
  public void setParameter(PreparedStatement ps, int i, T parameter, JdbcType jdbcType) throws SQLException {
    ps.setObject(i, typeConverter.fromEnum(parameter));
  }

  @Override
  public T getResult(ResultSet rs, String columnName) throws SQLException {
    return toEnum((K) rs.getObject(columnName));
  }

  @Override
  public T getResult(ResultSet rs, int columnIndex) throws SQLException {
    return toEnum((K) rs.getObject(columnIndex));
  }

  @Override
  public T getResult(CallableStatement cs, int columnIndex) throws SQLException {
    return toEnum((K) cs.getObject(columnIndex));
  }

  private T toEnum(K termIndex) {
    return typeConverter.toEnum(termIndex);
  }
}
