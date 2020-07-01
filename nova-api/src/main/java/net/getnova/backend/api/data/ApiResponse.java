package net.getnova.backend.api.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Data
@AllArgsConstructor
public class ApiResponse {

    private final String tag;
    private final ApiResponseCode responseCode;
    private final String message;
    private final Object data;

    public ApiResponse(@NotNull final ApiResponseCode responseCode) {
        this((String) null, responseCode);
    }

    public ApiResponse(@Nullable final String tag, @NotNull final ApiResponseCode responseCode) {
        this(tag, responseCode, null, null);
    }

    public ApiResponse(@NotNull final ApiResponseCode responseCode, @Nullable final String message) {
        this(null, responseCode, message);
    }

    public ApiResponse(@Nullable final String tag, @NotNull final ApiResponseCode responseCode, @Nullable final String message) {
        this(tag, responseCode, message, null);
    }

    public ApiResponse(@NotNull final ApiResponseCode responseCode, @Nullable final Object data) {
        this(null, responseCode, data);
    }

    public ApiResponse(@Nullable final String tag, @NotNull final ApiResponseCode responseCode, @Nullable final Object data) {
        this(tag, responseCode, null, data);
    }
}
