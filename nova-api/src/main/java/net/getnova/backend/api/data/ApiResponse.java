package net.getnova.backend.api.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import net.getnova.backend.json.JsonBuilder;
import net.getnova.backend.json.JsonSerializable;
import net.getnova.backend.json.JsonUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Getter
@EqualsAndHashCode
public final class ApiResponse implements JsonSerializable {

    private final String tag;
    private final ApiResponseStatus responseCode;
    private final String message;
    private final JsonElement data;

    public ApiResponse(@NotNull final ApiResponseStatus responseCode) {
        this((String) null, responseCode);
    }

    public ApiResponse(@Nullable final String tag, @NotNull final ApiResponseStatus responseCode) {
        this(tag, responseCode, null, null);
    }

    public ApiResponse(@NotNull final ApiResponseStatus responseCode, @Nullable final String message) {
        this(null, responseCode, message);
    }

    public ApiResponse(@Nullable final String tag, @NotNull final ApiResponseStatus responseCode, @Nullable final String message) {
        this(tag, responseCode, message, null);
    }

    public ApiResponse(@NotNull final ApiResponseStatus responseCode, @Nullable final Object data) {
        this(null, responseCode, data);
    }

    public ApiResponse(@Nullable final String tag, @NotNull final ApiResponseStatus responseCode, @Nullable final Object data) {
        this(tag, responseCode, null, data);
    }

    public ApiResponse(@NotNull final ApiResponseStatus responseCode, @Nullable final String message, @Nullable final Object data) {
        this(null, responseCode, message, data);
    }

    public ApiResponse(@Nullable final String tag, @NotNull final ApiResponseStatus responseCode, @Nullable final String message, @Nullable final Object data) {
        this.tag = tag;
        this.responseCode = responseCode;
        this.message = message;
        this.data = JsonUtils.toJson(data);
    }

    @NotNull
    @Override
    public JsonElement serialize() {
        final JsonBuilder info = JsonBuilder.create("tag", this.tag != null, this::getTag)
                .add("status", this.getResponseCode())
                .add("message", this.message != null, this::getMessage);

        return JsonBuilder.create("info", info)
                .add("data", !(this.data instanceof JsonNull), this::getData)
                .build();
    }
}
