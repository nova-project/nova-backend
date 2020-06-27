package net.getnova.backend.api.data;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiResponse {

    private final byte[] tag;
    private final ApiResponseCode responseCode;
    private final String message;
    private final Object data;

    public ApiResponse(final ApiResponseCode responseCode) {
        this((byte[]) null, responseCode);
    }

    public ApiResponse(final byte[] tag, final ApiResponseCode responseCode) {
        this(tag, responseCode, null, null);
    }

    public ApiResponse(final ApiResponseCode responseCode, final String message) {
        this(null, responseCode, message);
    }

    public ApiResponse(final byte[] tag, final ApiResponseCode responseCode, final String message) {
        this(tag, responseCode, message, null);
    }

    public ApiResponse(final ApiResponseCode responseCode, final Object data) {
        this(null, responseCode, data);
    }

    public ApiResponse(final byte[] tag, final ApiResponseCode responseCode, final Object data) {
        this(tag, responseCode, null, data);
    }
}
