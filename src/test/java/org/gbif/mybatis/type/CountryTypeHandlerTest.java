package org.gbif.mybatis.type;

import org.gbif.api.exception.ServiceUnavailableException;
import org.gbif.api.vocabulary.Country;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CountryTypeHandlerTest {

  @Mock
  private ResultSet mockedRs;
  private final CountryTypeHandler th = new CountryTypeHandler();

  @Test
  public void testGetResult() throws ServiceUnavailableException {
    try {
      when(mockedRs.getObject("country_i")).thenReturn("dk");
      assertThat(th.getResult(mockedRs, "country_i"), is(Country.DENMARK));
    } catch (SQLException e) {
    }
  }
}
