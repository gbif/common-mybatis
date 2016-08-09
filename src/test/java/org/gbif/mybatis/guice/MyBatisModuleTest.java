package org.gbif.mybatis.guice;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import javax.sql.DataSource;

import com.codahale.metrics.MetricRegistry;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.name.Names;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNotNull;

public class MyBatisModuleTest {

  public static class MyBatisModuleImpl extends MyBatisModule {

    public MyBatisModuleImpl(Properties properties) {
      super(properties);
    }

    public MyBatisModuleImpl(Properties properties, String dsKey) {
      super(properties, dsKey, new MetricRegistry());
    }

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
    MyBatisModule m = new MyBatisModuleImpl(new Properties());
  }

  private DataSource provideDs(String dsKey) {
    Properties  props = new Properties();
    props.setProperty("dataSourceClassName", "org.h2.jdbcx.JdbcDataSource");
    props.setProperty("dataSource.user", "sa");
    props.setProperty("dataSource.password", "sa");
    props.setProperty("dataSource.url", "jdbc:h2:mem:");
    props.setProperty("mybatis.configuration.callSettersOnNulls", "true");

    MyBatisModule m;
    if (dsKey != null) {
      m = new MyBatisModuleImpl(props, dsKey);
    } else {
      m = new MyBatisModuleImpl(props);
    }
    Injector inj = Guice.createInjector(m);
    DataSource ds = inj.getInstance(DataSource.class);

    //test additional myBatis configuration set through properties
    assertEquals("true", inj.getInstance(Key.get(String.class, Names.named("mybatis.configuration.callSettersOnNulls"))));

    return ds;
  }

  @Test
  public void testWithH2() throws SQLException {
    Connection c = provideDs(null).getConnection();
    assertNotNull(c);
    Statement st = c.createStatement();
    st.execute("create table test");
    c.close();
  }

  @Test
  public void testWithH2KeyedDatasource() throws SQLException {
    Connection c = provideDs("test").getConnection();
    assertNotNull(c);
    Statement st = c.createStatement();
    st.execute("create table test");
    c.close();
  }
}
