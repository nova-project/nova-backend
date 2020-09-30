package net.getnova.backend.jpa;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.pool.HikariPool;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import net.getnova.backend.boot.Bootstrap;
import net.getnova.backend.boot.module.Module;
import net.getnova.backend.boot.module.ModuleHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@Module
@ComponentScan
public class JpaModule {

  @Bean
  DataSource dataSource(final Bootstrap bootstrap, final JpaConfig config) {
    final HikariConfig hikariConfig = new HikariConfig();
    hikariConfig.setJdbcUrl("jdbc:" + config.getLocation() + "/" + config.getDatabase());
    hikariConfig.setUsername(config.getUsername());
    hikariConfig.setPassword(config.getPassword());

    hikariConfig.setAutoCommit(false);

    hikariConfig.setIdleTimeout(600000); // 10 minutes
    hikariConfig.setMaxLifetime(1800000); // 30 minutes
    hikariConfig.setConnectionTimeout(10000); // 10 secondsH
    hikariConfig.setInitializationFailTimeout(30000); // 30 seconds
    hikariConfig.setValidationTimeout(5000); // 5 seconds

    hikariConfig.setMaximumPoolSize(20);
    hikariConfig.setMinimumIdle(5);

    try {
      return new HikariDataSource(hikariConfig);
    } catch (HikariPool.PoolInitializationException e) {
      log.error("Unable to connect to database: {}", e.getMessage());
      bootstrap.shutdown();
      return null;
    }
  }

  @Bean
  LocalContainerEntityManagerFactoryBean entityManagerFactory(final ModuleHandler moduleHandler, final JpaConfig config, final DataSource dataSource) {
    final HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
    jpaVendorAdapter.setShowSql(config.isShowStatements());
    jpaVendorAdapter.setGenerateDdl(true);

    final LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
//    entityManagerFactoryBean.setJpaProperties(new SqlProperties(StandardCharsets.UTF_8));
    entityManagerFactoryBean.setJpaVendorAdapter(jpaVendorAdapter);
    entityManagerFactoryBean.setPackagesToScan(moduleHandler.getPackages().toArray(new String[0]));
    entityManagerFactoryBean.setDataSource(dataSource);

    return entityManagerFactoryBean;
  }

  @Bean
  PlatformTransactionManager transactionManager(final EntityManagerFactory entityManagerFactory) {
    return new JpaTransactionManager(entityManagerFactory);
  }
}
