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
import org.apache.ibatis.session.SqlSessionManager;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Basic mybatis module using HikariCP that takes all settings via the constructors properties instance.
 * The following minimal properties are required:
 * <ul>
 *   <li>{@code dataSourceClassName}, see https://github.com/brettwooldridge/HikariCP#popular-datasource-class-names</li>
 *   <li>{@code dataSource.serverName}</li>
 *   <li>{@code dataSource.databaseName}</li>
 *   <li>{@code dataSource.user}</li>
 *   <li>{@code dataSource.password}</li>
 * </ul>
 *
 * In addition to all Hikari configurations this module adds an optional ehCache that can be turned on using
 *  {@code enableCache=true}
 * See https://github.com/brettwooldridge/HikariCP#configuration-knobs-baby
 */
public abstract class MyBatisModule extends org.mybatis.guice.MyBatisModule {

  private static final Logger LOG = LoggerFactory.getLogger(MyBatisModule.class);
  private static final String CACHE_PROPERTY = "enableCache";

  private final Key<DataSource> datasourceKey;
  private final Key<SqlSessionManager> sessionManagerKey;
  private final boolean bindDatasource;
  private final Properties properties;
  private final boolean useCache;

  /**
   * Creates a new mybatis module binding the Datasource and SqlSessionManager by its class directly.
   * The guice key for that binding is available through {@link #getDatasourceKey}.
   */
  public MyBatisModule(Properties properties) {
    datasourceKey = Key.get(DataSource.class);
    sessionManagerKey = Key.get(SqlSessionManager.class);
    bindDatasource = false;
    this.properties = properties;
    useCache = readCacheSetting();
  }

  /**
   * Creates a new mybatis module binding the Datasource and SqlSessionManager with a name.
   * The guice key for that binding is available through {@link #getDatasourceKey}.
   */
  public MyBatisModule(String datasourceBindingName, Properties properties) {
    datasourceKey = Key.get(DataSource.class, Names.named(datasourceBindingName));
    sessionManagerKey = Key.get(SqlSessionManager.class, Names.named(datasourceBindingName));
    bindDatasource = true;
    this.properties = properties;
    useCache = readCacheSetting();
  }

  private boolean readCacheSetting() {
    boolean cache = Boolean.parseBoolean(properties.getProperty(CACHE_PROPERTY));
    // hikari throws exception if unknown properties remain!
    properties.remove(CACHE_PROPERTY);
    return cache;
  }

  @Override
  protected void initialize() {
    LogFactory.useSlf4jLogging();

    // makes things like logo_url map to logoUrl
    bindConstant().annotatedWith(Names.named("mybatis.configuration.mapUnderscoreToCamelCase")).to(true);

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
      bind(sessionManagerKey).to(SqlSessionManager.class);
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
