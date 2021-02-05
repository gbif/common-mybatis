package org.gbif.mybatis.type;

import org.apache.ibatis.type.JdbcType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UuidTypeHandlerTest {

  private final UuidTypeHandler uth = new UuidTypeHandler();

  @Mock
  private ResultSet mockRs;

  @Mock
  private CallableStatement mockCs;

  @Mock
  private PreparedStatement stmt;

  private final UUID testUuid = UUID.fromString("b324e8e9-9a4c-44fa-8f1a-7f39ea7ab576");

  @Test
  public void testSetValidParameter() throws SQLException {
    uth.setParameter(stmt, 1, testUuid, JdbcType.OTHER);
    verify(stmt).setObject(1, testUuid.toString(), Types.OTHER);
  }

  @Test
  public void testSetNullParameter() throws SQLException {
    uth.setParameter(stmt, 1, null, JdbcType.OTHER);
    verify(stmt).setObject(1, null, Types.OTHER);
  }

  @Test
  public void testGetInvalidUuid() throws SQLException {
    when(mockRs.getString("resource_key")).thenReturn("invalid uuid");
    assertNull(uth.getResult(mockRs, "resource_key"));
  }

  @Test
  public void testGetNull() throws SQLException {
    when(mockRs.getString("resource_key")).thenReturn(null);
    assertNull(uth.getResult(mockRs, "resource_key"));
  }

  @Test
  public void testGetValidUuid() throws SQLException {
    when(mockRs.getString("resource_key")).thenReturn(testUuid.toString());
    UUID result = uth.getResult(mockRs, "resource_key");

    assertThat(result, equalTo(testUuid));
  }

  /**
   * lower case UUID should be the same as lower case UUID.
   */
  @Test
  public void testGetResultLowerCase() throws SQLException {
    when(mockRs.getString("resource_key")).thenReturn("b324e8e9-9a4c-44fa-8f1a-7f39ea7ab576");
    assertThat(uth.getResult(mockRs, "resource_key").toString(), is("b324e8e9-9a4c-44fa-8f1a-7f39ea7ab576"));

    when(mockCs.getString(1)).thenReturn("b324e8e9-9a4c-44fa-8f1a-7f39ea7ab576");
    assertThat(uth.getResult(mockCs, 1).toString(), is("b324e8e9-9a4c-44fa-8f1a-7f39ea7ab576"));
  }

  /**
   * Upper case UUID should be converted to lower case and test should be successful.
   */
  @Test
  public void testGetResultUpperCase() throws SQLException {
    when(mockRs.getString("resource_key")).thenReturn("B324E8E9-9A4C-44FA-8F1A-7F39EA7AB576");
    assertThat(uth.getResult(mockRs, "resource_key").toString(), is("b324e8e9-9a4c-44fa-8f1a-7f39ea7ab576"));

    when(mockCs.getString(1)).thenReturn("B324E8E9-9A4C-44FA-8F1A-7F39EA7AB576");
    assertThat(uth.getResult(mockCs, 1).toString(), is("b324e8e9-9a4c-44fa-8f1a-7f39ea7ab576"));
  }

  /**
   * Different UUIDs should not be equal.
   */
  @Test
  public void testGetResultNotEqual() throws SQLException {
    when(mockRs.getString("resource_key")).thenReturn("14c5f0f0-1dab-11d9-8435-b8a03c50a862");
    assertThat(uth.getResult(mockRs, "resource_key").toString(), not("b324e8e9-9a4c-44fa-8f1a-7f39ea7ab576"));

    when(mockCs.getString(1)).thenReturn("14c5f0f0-1dab-11d9-8435-b8a03c50a862");
    assertThat(uth.getResult(mockCs, 1).toString(), not("b324e8e9-9a4c-44fa-8f1a-7f39ea7ab576"));
  }
}
