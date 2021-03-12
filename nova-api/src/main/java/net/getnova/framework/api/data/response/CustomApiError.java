package net.getnova.framework.api.data.response;

import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class CustomApiError implements ApiError {

  private final String target;
  private final String message;

  @Override
  public String getTarget() {
    return this.target;
  }

  @Override
  public String getMessage() {
    return this.message;
  }
}
