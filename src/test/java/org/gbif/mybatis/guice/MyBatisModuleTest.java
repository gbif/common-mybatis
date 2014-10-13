package org.gbif.mybatis.guice;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import javax.sql.DataSource;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class MyBatisModuleTest {

  public static class MyBatisModuleImpl extends MyBatisModule {

    public MyBatisModuleImpl(Properties properties) {
      super(properties);
    }

    public MyBatisModuleImpl(Properties properties, String dsKey) {
      super(dsKey, properties);
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
    props.setProperty("enableCache", "true");

    MyBatisModule m;
    if (dsKey != null) {
      m = new MyBatisModuleImpl(props, dsKey);
    } else {
      m = new MyBatisModuleImpl(props);
    }
    Injector inj = Guice.createInjector(m);
    DataSource ds = inj.getInstance(DataSource.class);

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
