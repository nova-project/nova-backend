package net.getnova.framework.api.ws;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.netty.handler.codec.http.HttpResponseStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import net.getnova.framework.api.data.response.ApiError;
import net.getnova.framework.api.data.response.ApiResponse;
import net.getnova.framework.api.data.response.DataApiResponse;
import net.getnova.framework.api.data.response.DefaultApiResponse;
import net.getnova.framework.api.data.response.ErrorApiResponse;

public interface WebsocketApiResponse {

  static WebsocketApiResponse of(final WebsocketApiRequest request, final ApiResponse response) {
    if (response instanceof DataApiResponse) {
      return new DataResponse((DataApiResponse) response, request);
    }

    if (response instanceof ErrorApiResponse) {
      return new ErrorResponse((ErrorApiResponse) response, request);
    }

    if (response instanceof DefaultApiResponse) {
      return new DefaultResponse((DefaultApiResponse) response, request);
    }

    throw new IllegalArgumentException("unknown response type: " + response.getClass());
  }

  @JsonSerialize(using = HttpResponseStatusSerializer.class)
  HttpResponseStatus getStatus();

  String getTag();

  @Data
  @AllArgsConstructor
  @RequiredArgsConstructor
  class DefaultResponse implements WebsocketApiResponse {

    private final DefaultApiResponse response;
    private WebsocketApiRequest request;

    @Override
    public HttpResponseStatus getStatus() {
      return this.response.getStatus();
    }

    @Override
    public String getTag() {
      if (this.request == null) {
        return null;
      }
      return this.request.getTag();
    }
  }

  @Data
  @AllArgsConstructor
  @RequiredArgsConstructor
  class DataResponse implements WebsocketApiResponse {

    private final DataApiResponse response;
    private WebsocketApiRequest request;

    @Override
    public HttpResponseStatus getStatus() {
      return this.response.getStatus();
    }

    @Override
    public String getTag() {
      if (this.request == null) {
        return null;
      }
      return this.request.getTag();
    }

    public Object getData() {
      return this.response.getData();
    }
  }

  @Data
  @AllArgsConstructor
  @RequiredArgsConstructor
  class ErrorResponse implements WebsocketApiResponse {

    private final ErrorApiResponse response;
    private WebsocketApiRequest request;

    @Override
    public HttpResponseStatus getStatus() {
      return this.response.getStatus();
    }

    @Override
    public String getTag() {
      if (this.request == null) {
        return null;
      }
      return this.request.getTag();
    }

    public ApiError[] getErrors() {
      return this.response.getErrors();
    }
  }
}
