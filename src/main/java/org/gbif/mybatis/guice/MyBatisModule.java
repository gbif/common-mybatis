package org.gbif.mybatis.guice;

import java.util.Properties;
import javax.sql.DataSource;

import com.google.inject.Key;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Names;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Basic mybatis module using HikariCP that takes all settings via the constructors properties instance.
 * The following minimal properties are required:
 * <ul>
 *   <li>dataSourceClassName, see https://github.com/brettwooldridge/HikariCP#popular-datasource-class-names</li>
 * </ul>
 *
 * In addition to all Hikari configurations this module adds an optional ehCache that can be turned on using
 *  enableCache=true
 * See https://github.com/brettwooldridge/HikariCP#configuration-knobs-baby
 */
public abstract class MyBatisModule extends org.mybatis.guice.MyBatisModule {

  private static final Logger LOG = LoggerFactory.getLogger(MyBatisModule.class);

  private final Key<DataSource> datasourceKey;
  private final boolean bindDatasource;
  private final Properties properties;

  /**
   * Creates a new mybatis module binding the Datasource by its class directly.
   * The guice key for that binding is available through {@link #getDatasourceKey}.
   */
  public MyBatisModule(Properties properties) {
    datasourceKey = Key.get(DataSource.class);
    bindDatasource = false;
    this.properties = properties;
  }

  /**
   * Creates a new mybatis module binding the Datasource with a name.
   * The guice key for that binding is available through {@link #getDatasourceKey}.
   */
  public MyBatisModule(String datasourceBindingName, Properties properties) {
    datasourceKey = Key.get(DataSource.class, Names.named(datasourceBindingName));
    bindDatasource = true;
    this.properties = properties;
  }

  @Override
  protected void initialize() {
    LogFactory.useSlf4jLogging();

    // makes things like logo_url map to logoUrl
    bindConstant().annotatedWith(Names.named("mybatis.configuration.mapUnderscoreToCamelCase")).to(true);

    final boolean useCache = Boolean.parseBoolean(properties.getProperty("enableCache"));
    useCacheEnabled(useCache);
    if (useCache) {
      LOG.info("Configuring MyBatis with cache");
      environmentId("production");
    } else {
      LOG.info("Configuring MyBatis with no cache");
      environmentId("development");
    }

    bindTransactionFactoryType(JdbcTransactionFactory.class);
    bindMappers();
    bindTypeHandlers();
    bindManagers();

    if (bindDatasource) {
      bind(datasourceKey).to(DataSource.class);
    }
  }

  @Provides
  @Singleton
  public DataSource provideDatasource() {
    HikariConfig config = new HikariConfig(properties);
    HikariDataSource ds = new HikariDataSource(config);
    return ds;
  }

  /**
   * @return the guice key for the bound datasource using a named binding if provided in constructor
   */
  public Key<DataSource> getDatasourceKey() {
    return datasourceKey;
  }

  /**
   * Implement method to bind the mybatis mappers to be used.
   */
  protected abstract void bindMappers();

  /**
   * Implement method to bind the mybatis type handlers to be used.
   */
  protected abstract void bindTypeHandlers();

  /**
   * Implement method to bind the managers/services.
   */
  protected abstract void bindManagers();

}
