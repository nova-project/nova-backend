package net.getnova.framework.api.data;

import java.util.Map;
import lombok.Data;

@Data
public abstract class AbstractApiRequest implements ApiRequest {

  private Map<String, String> pathVariables;

  @Override
  public Map<String, String> getPathVariables() {
    if (this.pathVariables == null) {
      throw new IllegalStateException("variables are not parsed");
    }

    return this.pathVariables;
  }

  @Override
  public void setPathVariables(final Map<String, String> variables) {
    if (this.pathVariables != null) {
      throw new IllegalStateException("variables already parsed");
    }

    this.pathVariables = variables;
  }

  @Override
  public String getPathVariable(final String name) {
    if (this.pathVariables == null) {
      throw new IllegalStateException("variables are not parsed");
    }

    return this.pathVariables.get(name);
  }
}
