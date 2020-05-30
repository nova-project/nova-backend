package net.getnova.backend.sql;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.PostgreSQL10Dialect;

import java.sql.Driver;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum SqlServerType {

    POSTGRES_10("postgresql", org.postgresql.Driver.class, PostgreSQL10Dialect.class);

    private final String urlPrefix;
    private final Class<? extends Driver> driver;
    private final Class<? extends Dialect> dialect;
}
