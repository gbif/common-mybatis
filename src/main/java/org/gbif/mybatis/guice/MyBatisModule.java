package org.gbif.mybatis.guice;

import org.gbif.utils.file.properties.PropertiesUtil;

import java.util.Properties;
import javax.annotation.Nullable;
import javax.sql.DataSource;

import com.codahale.metrics.MetricRegistry;
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
 * In addition to all Hikari configurations this module can add optional MyBatis configurations using
 * the mybatis.configuration. prefix.
 * {@code mybatis.configuration.callSettersOnNulls}
 * {@see http://www.mybatis.org/mybatis-3/configuration.html}
 */
public abstract class MyBatisModule extends org.mybatis.guice.MyBatisModule {

  private static final Logger LOG = LoggerFactory.getLogger(MyBatisModule.class);
  private static final String DEFAULT_MYBATIS_ENV_ID = "default";
  private static final String MYBATIS_ENV_ID = "mybatis.environment.id";
  private static final String MYBATIS_CFG_PREFIX = "mybatis.configuration.";

  private final Key<DataSource> datasourceKey;
  private final Key<SqlSessionManager> sessionManagerKey;
  private final boolean bindDatasource;
  private final Properties properties;
  private final MetricRegistry metricRegistry;
  //Used for dropwizard health checks
  private final Object healthCheckRegistry;


  /**
   * Creates a new mybatis module binding the Datasource and SqlSessionManager by its class directly.
   * The guice key for that binding is available through {@link #getDatasourceKey}.
   */
  public MyBatisModule(Properties properties) {
    this(properties, null, null, null);
  }

  /**
   * Creates a new mybatis module binding the Datasource and SqlSessionManager with a name.
   * The guice key for that binding is available through {@link #getDatasourceKey}.
   */
  public MyBatisModule(String datasourceBindingName, Properties properties) {
    this(properties, datasourceBindingName, null, null);
  }

  /**
   * Creates a new mybatis module binding the Datasource and SqlSessionManager with a name.
   * The guice key for that binding is available through {@link #getDatasourceKey}.
   * @param metricRegistry optional metric registry to expose hikari pool metrics
   */
  public MyBatisModule(Properties properties, @Nullable String datasourceBindingName,
                       @Nullable MetricRegistry metricRegistry,
                       @Nullable Object healthCheckRegistry) {
    bindDatasource = datasourceBindingName != null;
    if (bindDatasource) {
      datasourceKey = Key.get(DataSource.class, Names.named(datasourceBindingName));
      sessionManagerKey = Key.get(SqlSessionManager.class, Names.named(datasourceBindingName));
    } else {
      datasourceKey = Key.get(DataSource.class);
      sessionManagerKey = Key.get(SqlSessionManager.class);
    }
    this.properties = properties;
    this.metricRegistry = metricRegistry;
    this.healthCheckRegistry = healthCheckRegistry;
  }

  @Override
  protected void initialize() {
    LogFactory.useSlf4jLogging();

    // makes things like logo_url map to logoUrl
    mapUnderscoreToCamelCase(true);

    // change MyBatis environment ID or set the default one
    String myBatisEnvId = properties.getProperty(MYBATIS_ENV_ID, DEFAULT_MYBATIS_ENV_ID);
    environmentId(myBatisEnvId);
    LOG.debug("Configuring MyBatis environmentId {}", myBatisEnvId);

    // check if some MyBatis configuration are provided
    Properties myBatisConfig = PropertiesUtil.removeProperties(properties, MYBATIS_CFG_PREFIX);
    Names.bindProperties(binder(), myBatisConfig);

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
    if (metricRegistry != null) {
      config.setMetricRegistry(metricRegistry);
    }
    if (healthCheckRegistry != null) {
      config.setHealthCheckRegistry(healthCheckRegistry);
    }
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
   * @return the guice key for the bound sql session manager using a named binding if provided in constructor
   */
  public Key<SqlSessionManager> getSessionManagerKey() {
    return sessionManagerKey;
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
