package org.gbif.mybatis.type;

import org.gbif.api.vocabulary.Language;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LanguageTypeHandlerTest {

  @Mock
  private ResultSet mockedRs;
  private final LanguageTypeHandler th = new LanguageTypeHandler();

  @Test
  public void testGetResult() throws Exception {
    when(mockedRs.getObject("language_i")).thenReturn("en");
    assertEquals(Language.ENGLISH, th.getResult(mockedRs, "language_i"));
  }
}
