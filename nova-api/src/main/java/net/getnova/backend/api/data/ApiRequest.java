package net.getnova.backend.api.data;

import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiRequest {

    private final String endpoint;
    private final JsonObject data;
    private String tag;
}
