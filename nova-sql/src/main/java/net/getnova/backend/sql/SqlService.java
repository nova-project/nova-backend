package net.getnova.backend.sql;

import lombok.extern.slf4j.Slf4j;
import net.getnova.backend.Nova;
import net.getnova.backend.config.ConfigService;
import net.getnova.backend.injection.InjectionHandler;
import net.getnova.backend.service.Service;
import net.getnova.backend.service.event.PreInitService;
import net.getnova.backend.service.event.PreInitServiceEvent;
import net.getnova.backend.service.event.StartService;
import net.getnova.backend.service.event.StartServiceEvent;
import net.getnova.backend.service.event.StopService;
import net.getnova.backend.service.event.StopServiceEvent;
import net.getnova.backend.sql.model.TableModel;
import org.hibernate.Session;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

@Slf4j
@Service(id = "sql", depends = {ConfigService.class})
@Singleton
public final class SqlService {

    private static final Charset SQL_CHARSET = StandardCharsets.UTF_8;

    private final SqlProperties properties;
    private final SqlConfig config;
    private boolean validConfig = true;
    private boolean error = false;
    private SqlSessionFactory sessionFactory;

    @Inject
    private InjectionHandler injectionHandler;

    @Inject
    private Nova nova;

    /**
     * Creates a new {@link SqlService}.
     *
     * @param configService an instance of the {@link ConfigService},
     *                      it is injected by the dependency injection.
     */
    @Inject
    public SqlService(final ConfigService configService) {
        this.properties = new SqlProperties(SQL_CHARSET);
        this.config = configService.addConfig("sql", new SqlConfig());
    }

    @PreInitService
    private void preInit(final PreInitServiceEvent event) {
        SqlServerType serverType = null;
        try {
            serverType = SqlServerType.valueOf(this.config.getServerType().toUpperCase());
        } catch (IllegalArgumentException ignored) {
            log.error("Can't found sql serverType type {}.", this.config.getServerType());
            this.validConfig = false;
        }

        if (this.validConfig) {
            this.properties.setServer(serverType, this.config.getLocation(), this.config.getDatabase());
            this.properties.setUser(this.config.getUsername(), this.config.getPassword());

            this.sessionFactory = new SqlSessionFactory(this.properties);
        }
    }

    @StartService
    private void start(final StartServiceEvent event) {
        if (this.validConfig) {
            try {
                this.sessionFactory.build();
            } catch (SqlException e) {
                this.error = true;
                log.error("Unable to connect to the database server.", e);
            }
        }
    }

    @StopService
    private void stop(final StopServiceEvent event) {
        if (this.validConfig && !error) this.sessionFactory.close();
    }

    /**
     * Opens a new Sql-{@link Session}.
     *
     * @return the {@link Session}
     */
    public Session openSession() {
        return this.sessionFactory.openSession();
    }

    public void addEntity(final Class<? extends TableModel> entity) {
        try {
            this.sessionFactory.addEntity(entity);
        } catch (SqlException e) {
            log.error("Unable to register entity " + entity.getName() + ".", e);
        }
    }
}
