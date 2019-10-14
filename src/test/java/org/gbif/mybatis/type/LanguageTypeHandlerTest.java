package org.gbif.mybatis.type;

import org.gbif.api.exception.ServiceUnavailableException;
import org.gbif.api.vocabulary.Language;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LanguageTypeHandlerTest {

  @Mock
  private ResultSet mockedRs;
  private final LanguageTypeHandler th = new LanguageTypeHandler();

  @Test
  public void testGetResult() throws ServiceUnavailableException {
    try {
      when(mockedRs.getObject("language_i")).thenReturn("en");
      assertThat(th.getResult(mockedRs, "language_i"), is(Language.ENGLISH));
    } catch (SQLException e) {
    }
  }
}
