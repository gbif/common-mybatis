package org.gbif.mybatis.type;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.gbif.common.parsers.core.UrlParser;

import java.net.URI;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * A simple converter for varchars to URI.
 */
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
