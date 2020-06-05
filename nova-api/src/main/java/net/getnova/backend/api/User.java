package net.getnova.backend.api;

import lombok.Data;
import net.getnova.backend.api.annotations.ApiField;
import net.getnova.backend.api.annotations.ApiType;

import java.time.ZonedDateTime;

@Data
@ApiType(name = "User", queryName = "users", description = "A user is a person who interacts with the app. It is also used to store who has access to content or who created it, e.g. settings or authored texts.")
public class User extends IdField {

    @ApiField(name = "name", description = "The name of a user is used so that the user can be identified by other users without them having to know the id's.")
    private String name;

    @ApiField(name = "created", description = "The account creation date indicates when the user created his account.")
    private ZonedDateTime created;

    @ApiField(name = "last", description = "The `last` date indicates when the user last interacted with an api. This can be used, for example, to show when a user was last logged in.")
    private ZonedDateTime last;
}
