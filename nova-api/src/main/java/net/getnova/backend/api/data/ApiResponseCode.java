package net.getnova.backend.api.data;

import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString
@RequiredArgsConstructor
public enum ApiResponseCode {

    // Information responses 1xx

    // Successful responses 2xx
    OK(200, "OK", false),

    // Redirection messages 3xx

    // Client error responses 4xx
    BAD_REQUEST(400, "BAD_REQUEST", true),
    UNAUTHORIZED(401, "UNAUTHORIZED", true),
    FORBIDDEN(403, "FORBIDDEN", true),
    NOT_FOUND(404, "NOT_FOUND", true),

    // Server error responses 5xx
    INTERNAL_SERVER_ERROR(500, "INTERNAL_SERVER_ERROR", true),
    NOT_IMPLEMENTED(501, "NOT_IMPLEMENTED", true);

    // Self made 9xx

    private final int code;
    private final String name;
    private final boolean error;
}
