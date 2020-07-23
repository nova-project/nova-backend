package net.getnova.backend.api.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import net.getnova.backend.json.JsonBuilder;
import net.getnova.backend.json.JsonSerializable;
import net.getnova.backend.json.JsonUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Getter
@EqualsAndHashCode
public final class ApiResponse implements JsonSerializable {

    private final ApiResponseStatus responseCode;
    private final String message;
    private final JsonElement data;
    @Setter
    private String tag;

    public ApiResponse(@NotNull final ApiResponseStatus responseCode) {
        this(responseCode, null, null);
    }

    public ApiResponse(@NotNull final ApiResponseStatus responseCode, @Nullable final String message) {
        this(responseCode, message, null);
    }

    public ApiResponse(@NotNull final ApiResponseStatus responseCode, @Nullable final Object data) {
        this(responseCode, null, data);
    }

    public ApiResponse(@NotNull final ApiResponseStatus responseCode, @Nullable final String message, @Nullable final Object data) {
        this.responseCode = responseCode;
        this.message = message;
        this.data = JsonUtils.toJson(data);
    }

    @NotNull
    @Override
    public JsonElement serialize() {
        return this.serialize(false);
    }

    @NotNull
    public JsonElement serialize(final boolean small) {
        final JsonBuilder info = small
                ? JsonBuilder.create("error", this.responseCode.isError())
                .add("message", this.message != null, () -> this.message)
                : JsonBuilder.create("tag", this.getTag())
                .add("status", this.getResponseCode())
                .add("message", this.message != null, this::getMessage);

        return JsonBuilder.create("info", info)
                .add("data", !(this.data instanceof JsonNull), this::getData)
                .build();
    }
}
