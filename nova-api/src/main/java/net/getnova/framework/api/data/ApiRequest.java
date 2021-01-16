package net.getnova.framework.api.data;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class ApiRequest {

  private final String endpoint;
  private final JsonNode data;
  private String tag;
}
