package net.getnova.backend.sql;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.MariaDB103Dialect;
import org.hibernate.dialect.PostgreSQL10Dialect;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum SqlServerType {

    POSTGRES("postgresql", "org.postgresql.Driver", PostgreSQL10Dialect.class),
    MARIADB("mariadb", "org.mariadb.jdbc.Driver", MariaDB103Dialect.class);

    private final String urlPrefix;
    private final String driver;
    private final Class<? extends Dialect> dialect;
}
