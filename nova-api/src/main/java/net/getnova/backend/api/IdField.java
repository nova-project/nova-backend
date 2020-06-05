package net.getnova.backend.api;

import lombok.Data;
import net.getnova.backend.api.annotations.ApiField;
import net.getnova.backend.api.annotations.ApiSupperType;

import java.util.UUID;

@Data
@ApiSupperType
public class IdField {

    @ApiField(name = "id", description = "The id is a **unique** id (`uuid`) which is used for internal identification of elements or records.")
    private UUID uuid;
}
