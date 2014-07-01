package org.gbif.mybatis.guice;

import javax.sql.DataSource;

import com.google.inject.Inject;
import com.google.inject.Key;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.mybatis.guice.datasource.bonecp.BoneCPProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Basic mybatis module using BoneCP and offers an optional Named guice setting to turn on caching by binding
 * a Boolean "enableCache".
 */
public abstract class MyBatisModule extends org.mybatis.guice.MyBatisModule {

  private static final Logger LOG = LoggerFactory.getLogger(MyBatisModule.class);

  @Inject(optional = true)
  @Named("enableCache")
  private Boolean useCache;
  private final Key<DataSource> datasourceKey;
  private final boolean bindDatasource;

  protected boolean useCache() {
    return useCache != null && useCache;
  }

  /**
   * Creates a new mybatis module binding the Datasource by its class directly.
   * The guice key for that binding is available through {@link #getDatasourceKey}.
   */
  public MyBatisModule() {
    datasourceKey = Key.get(DataSource.class);
    bindDatasource = false;
  }

  /**
   * Creates a new mybatis module binding the Datasource with a name.
   * The guice key for that binding is available through {@link #getDatasourceKey}.
   */
  public MyBatisModule(String datasourceBindingName) {
    datasourceKey = Key.get(DataSource.class, Names.named(datasourceBindingName));
    bindDatasource = true;
  }

  @Override
  protected void initialize() {
    LogFactory.useSlf4jLogging();

    // makes things like logo_url map to logoUrl
    bindConstant().annotatedWith(Names.named("mybatis.configuration.mapUnderscoreToCamelCase")).to(true);

    useCacheEnabled(useCache());
    if (useCache()) {
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

    bindDataSourceProviderType(BoneCPProvider.class);

    if (bindDatasource) {
      bind(datasourceKey).to(DataSource.class);
    }
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
