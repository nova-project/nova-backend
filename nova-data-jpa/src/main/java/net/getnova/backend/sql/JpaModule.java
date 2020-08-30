package net.getnova.backend.sql;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.pool.HikariPool;
import lombok.extern.slf4j.Slf4j;
import net.getnova.backend.boot.Bootstrap;
import net.getnova.backend.boot.module.Module;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.nio.charset.StandardCharsets;

@Slf4j
@Module
@ComponentScan
public class JpaModule {

  private final Bootstrap bootstrap;
  private final SqlConfig config;

  public JpaModule(final Bootstrap bootstrap, final SqlConfig config) {
    this.bootstrap = bootstrap;
    this.config = config;
  }

  @Bean
  public DataSource dataSource() {
    final HikariConfig config = new HikariConfig();

    config.setJdbcUrl("jdbc:" + this.config.getLocation() + "/" + this.config.getDatabase());
    config.setUsername(this.config.getUsername());
    config.setPassword(this.config.getPassword());

    config.setAutoCommit(false);

    config.setIdleTimeout(600000); // 10 minutes
    config.setMaxLifetime(1800000); // 30 minutes
    config.setConnectionTimeout(10000); // 10 seconds
    config.setInitializationFailTimeout(30000); // 30 seconds
    config.setValidationTimeout(5000); // 5 seconds

    config.setMaximumPoolSize(20);
    config.setMinimumIdle(5);

    try {
      return new HikariDataSource(config);
    } catch (HikariPool.PoolInitializationException e) {
      log.error("Unable to connect to database: {}", e.getMessage());
      this.bootstrap.shutdown();
      return null;
    }
  }

  @Bean
  public LocalContainerEntityManagerFactoryBean entityManagerFactory(final DataSource dataSource) {
    final LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
    entityManagerFactoryBean.setDataSource(dataSource);
    entityManagerFactoryBean.setPackagesToScan("net.getnova");

    final HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
//    jpaVendorAdapter.setPrepareConnection();
//    jpaVendorAdapter.setGenerateDdl(true);
    jpaVendorAdapter.setShowSql(this.config.isShowStatements());
    entityManagerFactoryBean.setJpaVendorAdapter(jpaVendorAdapter);
    entityManagerFactoryBean.setJpaProperties(new SqlProperties(StandardCharsets.UTF_8));
    return entityManagerFactoryBean;
  }

  @Bean
  public JpaTransactionManager transactionManager(final EntityManagerFactory entityManagerFactory) {
    return new JpaTransactionManager(entityManagerFactory);
  }
}
