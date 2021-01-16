package net.getnova.framework.api.handler.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.util.AsciiString;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.getnova.framework.api.data.ApiEndpointData;
import net.getnova.framework.api.data.ApiResponse;
import net.getnova.framework.api.executor.ApiExecutor;
import net.getnova.framework.json.JsonBuilder;
import net.getnova.framework.json.JsonUtils;
import net.getnova.framework.network.server.http.HttpUtils;
import net.getnova.framework.network.server.http.route.HttpRoute;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;
import reactor.netty.http.server.HttpServerRequest;
import reactor.netty.http.server.HttpServerResponse;

@Slf4j
@RequiredArgsConstructor
final class RestApiRoute implements HttpRoute {

  private static final Charset CHARSET = StandardCharsets.UTF_8;
  private static final AsciiString CONTENT_TYPE = AsciiString
    .of(HttpHeaderValues.APPLICATION_JSON + "; " + HttpHeaderValues.CHARSET + "=" + CHARSET.toString());
  private final Map<String, ApiEndpointData> endpoints;

  @Override
  public Publisher<Void> execute(final HttpServerRequest httpRequest, final HttpServerResponse httpResponse) {
    return HttpUtils.checkMethod(httpRequest, httpResponse, HttpMethod.GET, HttpMethod.POST)
      .orElseGet(() -> httpRequest.receive()
        .aggregate()
        .defaultIfEmpty(Unpooled.EMPTY_BUFFER) // TODO: remove this
        .flatMap(content -> this.execute(httpRequest, content))
        .onErrorResume(this::handleError)
        .flatMap(apiResponse -> Mono.justOrEmpty(this.parseResponse(httpResponse, apiResponse)))
        .transform(json -> httpResponse.sendString(json, CHARSET))
      );
  }

  private Mono<ApiResponse> execute(final HttpServerRequest httpRequest, final ByteBuf content) {
    final JsonNode node;

    final Charset charset = HttpUtil.getCharset(
      httpRequest.requestHeaders().get(HttpHeaderNames.CONTENT_TYPE),
      StandardCharsets.UTF_8
    );

    try (InputStream is = new ByteBufInputStream(content)) {
      node = JsonUtils.read(is, charset);
    }
    catch (JsonProcessingException e) {
      return Mono.just(new ApiResponse(HttpResponseStatus.BAD_REQUEST, "JSON_SYNTAX"));
    }
    catch (IOException e) {
      log.error("Unable to read data form remote stream");
      return Mono.just(new ApiResponse(HttpResponseStatus.INTERNAL_SERVER_ERROR));
    }

    return ApiExecutor.execute(
      this.endpoints,
      new RestApiRequest(httpRequest.path(), node, httpRequest)
    );
  }

  private Optional<String> parseResponse(final HttpServerResponse httpResponse, final ApiResponse apiResponse) {
    httpResponse.header(HttpHeaderNames.CONTENT_TYPE, CONTENT_TYPE);

    final AtomicReference<HttpResponseStatus> status = new AtomicReference<>();

    final Optional<String> json = apiResponse.getMessage()
      .map(msg -> (JsonNode) JsonBuilder.create("message", msg).build())
      .or(apiResponse::getJson)
      .map(node -> {
        try {
          final String jsonData = JsonUtils.toString(node);
          status.set(apiResponse.getStatus());
          return jsonData;
        }
        catch (JsonProcessingException e) {
          status.set(HttpResponseStatus.INTERNAL_SERVER_ERROR);
          log.error("Unable to write json.", e);
          return null;
        }
      });

    httpResponse.status(status.get());
    return json;
  }

  private Mono<ApiResponse> handleError(final Throwable cause) {
    log.error("Error while executing rest api pipeline.", cause);
    return Mono.just(new ApiResponse(HttpResponseStatus.INTERNAL_SERVER_ERROR));
  }
}
