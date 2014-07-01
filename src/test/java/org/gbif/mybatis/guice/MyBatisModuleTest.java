package org.gbif.mybatis.guice;

import org.junit.Test;

import static org.junit.Assert.assertFalse;

public class MyBatisModuleTest {

  public static class MyBatisModuleImpl extends MyBatisModule {

    @Override
    protected void bindMappers() {
    }

    @Override
    protected void bindTypeHandlers() {
    }

    @Override
    protected void bindManagers() {
    }
  }

  @Test
  public void testWithoutBindings() {
    MyBatisModule m = new MyBatisModuleImpl();
    assertFalse(m.useCache());
  }
}
