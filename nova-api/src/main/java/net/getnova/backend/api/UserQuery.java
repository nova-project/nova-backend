package net.getnova.backend.api;

import net.getnova.backend.api.annotations.ApiArgument;
import net.getnova.backend.api.annotations.ApiMutation;
import net.getnova.backend.api.annotations.ApiQuery;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class UserQuery {

    @ApiQuery(name = "users", description = "Return a list with all users.")
    private List<User> getUsers(
            @ApiArgument(name = "enabled", description = "if this parameter is enabled.", nullable = true) final boolean enabled
    ) {
        final User user = new User();
        user.setId(UUID.randomUUID());
        user.setName("Peter Hans");
        user.setCreated(ZonedDateTime.now());
        user.setLast(ZonedDateTime.now());

        return enabled ? List.of(user) : Collections.emptyList();
    }

    @ApiMutation(name = "addUser", description = "adds a new user")
    private User addUser(
            @ApiArgument(name = "name", description = "the name of the user") final String name
    ) {
        final User user = new User();
        user.setId(UUID.randomUUID());
        user.setName(name);
        user.setCreated(ZonedDateTime.now());
        user.setLast(ZonedDateTime.now());
        return user;
    }
}
