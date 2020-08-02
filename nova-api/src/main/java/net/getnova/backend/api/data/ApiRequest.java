package net.getnova.backend.api.data;

import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Data
@AllArgsConstructor
public final class ApiRequest {

    @NotNull
    private final String endpoint;
    @NotNull
    private final JsonObject data;
    @Nullable
    private String tag;
}
