package net.getnova.backend.sql;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import net.getnova.backend.config.ConfigValue;

@Data
@Setter(AccessLevel.NONE)
class SqlConfig {

    @ConfigValue(id = "location", comment = "Defines the location of the database server.")
    private String location = "//localhost:5432";

    @ConfigValue(id = "serverType", comment = "Defines the type of the sql server.")
    private String serverType = SqlServerType.POSTGRES_10.name();

    @ConfigValue(id = "database", comment = "Defines the database name which should be used.")
    private String database = "nova";

    @ConfigValue(id = "username", comment = "Defines the username which should be used.")
    private String username = "nova";

    @ConfigValue(id = "password", comment = "Defines the password which should be used.")
    private String password = "nova";
}
