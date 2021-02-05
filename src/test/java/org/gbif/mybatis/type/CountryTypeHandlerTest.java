package org.gbif.mybatis.type;

import org.gbif.api.vocabulary.Country;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CountryTypeHandlerTest {

  @Mock
  private ResultSet mockedRs;
  private final CountryTypeHandler th = new CountryTypeHandler();

  @Test
  public void testGetResult() throws Exception {
    when(mockedRs.getObject("country_i")).thenReturn("dk");
    assertEquals(Country.DENMARK, th.getResult(mockedRs, "country_i"));
  }
}
